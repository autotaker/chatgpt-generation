---
tags: Haskell ghc
title: GHCの融合変換を理解する(前編)
---
# 今回のお題
GHCで`sum [1,2,3]`はどのようにコンパイルされるでしょうか。

```Haskell
module Sum where
sum123 :: Int
sum123 = sum [1,2,3] 
```

コンパイルしてみると定数`6`となっていることがわかります。

```console
$ stack ghc -- -O Sum.hs
$ stack ghc -- --show-iface Sum.hi
(中略)
1eb3421a20d14a1255f6f5adccf8e3bd
  sum123 :: GHC.Types.Int
    {- HasNoCafRefs, Strictness: m, Unfolding: (GHC.Types.I# 6#) -}
```

今回の記事では、GHCはどのように6を計算しているのか解説します。ポイントはリストリテラルの脱糖と、fold/build変換です。

# リストリテラル
`ghc`に`-ddump-ds`オプションを渡すと脱糖の結果をみることができます。

`-O`オプションがない場合、GHCは`[1,2,3]`を`1 : 2: 3 : []`に脱糖します。

```console
$ stack ghc -- -ddump-ds Sum.hs
[1 of 1] Compiling Sum              ( Sum.hs, Sum.o ) [Optimisation flags changed]
==================== Desugar (after optimization) ====================
...
-- RHS size: {terms: 13, types: 6, coercions: 0, joins: 0/0}
sum123 :: Int
[LclIdX]
sum123
  = sum
      @ []
      Data.Foldable.$fFoldable[]
      @ Int
      GHC.Num.$fNumInt
      (GHC.Types.:
         @ Int
         (GHC.Types.I# 1#)
         (GHC.Types.:
            @ Int
            (GHC.Types.I# 2#)
            (GHC.Types.: @ Int (GHC.Types.I# 3#) (GHC.Types.[] @ Int))))
```

一方で、`-O`がある場合は`build (\c n -> c 1 (c 2 (c 3 n)))`という式に脱糖します。

```console
$ stack ghc -- -O -ddump-ds Sum.hs
[1 of 1] Compiling Sum              ( Sum.hs, Sum.o ) [Optimisation flags changed]
==================== Desugar (after optimization) ====================
-- RHS size: {terms: 17, types: 9, coercions: 0, joins: 0/0}
sum123 :: Int
[LclIdX]
sum123
  = sum
      @ []
      Data.Foldable.$fFoldable[]
      @ Int
      GHC.Num.$fNumInt
      (GHC.Base.build
         @ Int
         (\ (@ a_d1v6)
            (c_d1v7 [OS=OneShot] :: Int -> a_d1v6 -> a_d1v6)
            (n_d1v8 [OS=OneShot] :: a_d1v6) ->
            c_d1v7
              (GHC.Types.I# 1#)
              (c_d1v7 (GHC.Types.I# 2#) (c_d1v7 (GHC.Types.I# 3#) n_d1v8))))
```


ここで`build`は`GHC.Base`で定義される関数です。

```haskell
build :: (forall b. (a -> b -> b) -> b -> b) -> [a]
build g = g (:) []
```
`g c n`は、とあるリストの`(:)`コンストラクタを`c`に、`[]`コンストラクタを`n`にそれぞれ置き換える関数と考えると良いです。`build`はそのような`g`を受け取り、`c`として`(:)`、`n`として`[]`を渡すことでリストを生成します。

つまり、脱糖の結果、

```haskell
sum [1,2,3] ==> sum (build (\c n -> c 1 (c 2 (c 3 n))))
```
となります。

次に、`sum`関数です。これは(Foldable型クラスを具象化すると)以下のような定義になります。

```haskell
sum :: (Num a) => [a] -> a
sum = foldl (+) 0
```
次にfoldl関数の定義を見てみましょう。

```haskell
foldl :: forall a b. (b -> a -> b) -> b -> [a] -> b
{-# INLINE foldl #-}
foldl k z0 xs =
  foldr (\(v::a) (fn::b->b) -> oneShot (\(z::b) -> fn (k z v))) (id :: b -> b) xs z0
```
[ソース](http://hackage.haskell.org/package/base-4.12.0.0/docs/src/GHC.List.html#foldl)

驚くべきことに`foldl`は`foldr`を使って書かれています。その理由はコメントで書かれています。

> Note [Left folds via right fold]
>
> Implementing foldl et. al. via foldr is only a good idea if the compiler can
> optimize the resulting code (eta-expand the recursive "go"). See #7994.
> We hope that one of the two measure kick in:
>
>    * Call Arity (-fcall-arity, enabled by default) eta-expands it if it can see
>      all calls and determine that the arity is large.
>    * The oneShot annotation gives a hint to the regular arity analysis that
>      it may assume that the lambda is called at most once.
>      See [One-shot lambdas] in CoreArity and especially [Eta expanding thunks]
>      in CoreArity.
> The oneShot annotations used in this module are correct, as we only use them in
> arguments to foldr, where we know how the arguments are called.
> `oneShot`

要は、コンパイラがη展開してくれるから途中で生成されるクロージャは消えるので問題ないということです。そして、`foldl`が`foldr`で書かれていることにはある重要な理由があります。
それは**`fold/build`変換が効く**ということです。これについては後述します。

さて、ghcはこれらの定義に従ってコードをインライン展開します。

```haskell
sum [1,2,3] 
    ==> sum (build (\c n -> c 1 (c 2 (c 3 n)))) -- desugar
    ==> foldl (+) 0 (build (\c n -> c 1 (c 2 (c 3 n)))) -- inline sum
    ==> foldr (\v fn -> (\z -> fn ((+) z v))) id (build (\c n -> c 1 (c 2 (c 3 n)))) 0 -- inline foldl  
```
`oneShot`はコンパイラへのヒントで実態は`id`関数なので簡単のため取り除きました。

# fold/build変換
さて、fold/build変換は[ここ](http://hackage.haskell.org/package/base-4.12.0.0/docs/src/GHC.Base.html#line-1060)で定義されています。

```haskell
{-# RULES
"fold/build"    forall k z (g::forall b. (a->b->b) -> b -> b) .
                foldr k z (build g) = g k z
 #-}
```

まず`RULES`プラグマについて説明します。これは**左辺で書かれた式を右辺の式に書き換える**というGHCの持つ強力な~~(黒魔術)~~最適化機構です。今回の場合は「`foldr k z (build g)`の形をした式を`g k z`に書き換える」ということを意味しています。この書き換えが正しいこと、および書き換えが停止することは完全に**ライブラリ製作者の責任**です。

実際、このfold/build変換が正しいかどうかは自明ではありません。ほとんどの場合は問題ないことが知られていますが、`seq`と`undefined`をうまく使うと左辺と右辺で結果が異なる例が存在します。[参考](https://wiki.haskell.org/Correctness_of_short_cut_fusion)

さて、今回の式でもfold/build変換が適用できる形をしているので変換してみましょう。

```haskell
sum [1,2,3] 
    ==> foldr (\v fn -> (\z -> fn ((+) z v))) id (build (\c n -> c 1 (c 2 (c 3 n)))) 0 
    ==> (\c n -> c 1 (c 2 (c 3 n))) (\v fn -> (\z -> fn ((+) z v))) id 0 -- fold/build
```
実際の変換はghcに`-ddump-rule-rewrites`オプションを渡すと確認できます。

```console
$ stack ghc -- -O -ddump-rule-rewrites Sum.hs
...
Rule fired
    Rule: fold/build
    Module: (GHC.Base)
    Before: GHC.Base.foldr
              TyArg GHC.Types.Int
              TyArg GHC.Types.Int -> GHC.Types.Int
              ValArg \ (ds_a2Bp :: GHC.Types.Int)
                       (ds1_a2Bq [OS=OneShot] :: GHC.Types.Int -> GHC.Types.Int)
                       (v_a2Br [OS=OneShot] :: GHC.Types.Int) ->
                       ds1_a2Bq (GHC.Num.$fNumInt_$c+ v_a2Br ds_a2Bp)
              ValArg GHC.Base.id @ GHC.Types.Int
              ValArg GHC.Base.build
                       @ GHC.Types.Int
                       (\ (@ a_d1va)
                          (c_d1vb [OS=OneShot] :: GHC.Types.Int -> a_d1va -> a_d1va)
                          (n_d1vc [OS=OneShot] :: a_d1va) ->
                          c_d1vb
                            (GHC.Types.I# 1#)
                            (c_d1vb (GHC.Types.I# 2#) (c_d1vb (GHC.Types.I# 3#) n_d1vc)))
    After:  (\ (@ b_a2Cu)
               (@ a_a2Cv)
               (k_a2Cw :: a_a2Cv -> b_a2Cu -> b_a2Cu)
               (z_a2Cx :: b_a2Cu)
               (g_a2Cy :: forall b1. (a_a2Cv -> b1 -> b1) -> b1 -> b1) ->
               g_a2Cy @ b_a2Cu k_a2Cw z_a2Cx)
              @ (GHC.Types.Int -> GHC.Types.Int)
              @ GHC.Types.Int
              (\ (ds_a2Bp :: GHC.Types.Int)
                 (ds1_a2Bq [OS=OneShot] :: GHC.Types.Int -> GHC.Types.Int)
                 (v_a2Br [OS=OneShot] :: GHC.Types.Int) ->
                 ds1_a2Bq (GHC.Num.$fNumInt_$c+ v_a2Br ds_a2Bp))
              (GHC.Base.id @ GHC.Types.Int)
              (\ (@ a_d1va)
                 (c_d1vb [OS=OneShot] :: GHC.Types.Int -> a_d1va -> a_d1va)
                 (n_d1vc [OS=OneShot] :: a_d1va) ->
                 c_d1vb
                   (GHC.Types.I# 1#)
                   (c_d1vb (GHC.Types.I# 2#) (c_d1vb (GHC.Types.I# 3#) n_d1vc)))
    Cont:   ApplyToVal nodup z0_a2Bn
            Stop[RhsCtxt] GHC.Types.Int
```

fold/build変換の結果β簡約できる箇所が見つかりました。GHCは（簡約結果が大きくなりすぎない限り）このような簡約を自動で行います。
人手でやるのはしんどいですが実際にやってみましょう。

```haskell
sum [1,2,3] 
    ==> ...
    ==> (\c n -> c 1 (c 2 (c 3 n))) (\v fn -> (\z -> fn ((+) z v))) id 0 -- fold/build
    ==> (\v fn -> (\z -> fn ((+) z v))) 1              -- beta
         ((\v fn -> (\z -> fn ((+) z v))) 2  
           ((\v fn -> (\z -> fn ((+) z v))) 3 id)) 0 
    ==> (\v fn -> (\z -> fn ((+) z v))) 1              -- beta
         ((\v fn -> (\z -> fn ((+) z v))) 2 (\z -> id ((+) z 3))) 0 
    ==> (\v fn -> (\z -> fn ((+) z v))) 1              -- beta
         (\z -> (\z -> id ((+) z 3)) ((+) z 2)) 0 
    ==> (\v fn -> (\z -> fn ((+) z v))) 1 (\z -> id ((+) ((+) z 2) 3)) 0 --beta
    ==> (\z -> (\z -> id ((+) ((+) z 2) 3)) ((+) z 1)) 0 --beta
    ==> (\z -> id ((+) ((+) ((+) z 1) 2) 3)) 0 --beta
    ==> id ((+) ((+) ((+) 0 1) 2) 3) --beta
```
最後に`id`がインライン展開され、定数同士の演算がコンパイル時に行われることで、`sum123 ==> 6`となります。


# 結論
ghcではリストリテラルは`build`関数を使った定義に脱糖されるため、`fold/build`変換の対象となります。
最後に変換の全体結果を示します。

```haskell
sum [1,2,3] 
    ==> sum (build (\c n -> c 1 (c 2 (c 3 n)))) -- desugar
    ==> foldl (+) 0 (build (\c n -> c 1 (c 2 (c 3 n)))) -- inline sum
    ==> foldr (\v fn -> (\z -> fn ((+) z v))) id (build (\c n -> c 1 (c 2 (c 3 n)))) 0 -- inline foldl  
    ==> (\c n -> c 1 (c 2 (c 3 n))) (\v fn -> (\z -> fn ((+) z v))) id 0 -- fold/build
    ==> (\v fn -> (\z -> fn ((+) z v))) 1              -- beta
         ((\v fn -> (\z -> fn ((+) z v))) 2  
           ((\v fn -> (\z -> fn ((+) z v))) 3 id)) 0 
    ==> (\v fn -> (\z -> fn ((+) z v))) 1              -- beta
         ((\v fn -> (\z -> fn ((+) z v))) 2 (\z -> id ((+) z 3))) 0 
    ==> (\v fn -> (\z -> fn ((+) z v))) 1              -- beta
         (\z -> (\z -> id ((+) z 3)) ((+) z 2)) 0 
    ==> (\v fn -> (\z -> fn ((+) z v))) 1 (\z -> id ((+) ((+) z 2) 3)) 0 --beta
    ==> (\z -> (\z -> id ((+) ((+) z 2) 3)) ((+) z 1)) 0 --beta
    ==> (\z -> id ((+) ((+) ((+) z 1) 2) 3)) 0 --beta
    ==> id ((+) ((+) ((+) 0 1) 2) 3) --beta
    ==> ((+) ((+) ((+) 0 1) 2) 3) -- inline id
    ==> 6 -- simplify
```
後半ではfold/build変換についてより詳しく解説します。
