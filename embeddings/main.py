import os
import sys
import pandas as pd
import numpy as np
from sklearn.manifold import TSNE
import matplotlib.pyplot as plt
import matplotlib
import japanize_matplotlib
import budoux
import openai
from openai.embeddings_utils import get_embedding, cosine_similarity

openai.api_key = os.getenv("OPENAI_API_KEY")


def dataframe(files: list[str]) -> pd.DataFrame:
    records = []
    for file in files:
        record = load_metadata(file)
        with open(file) as f:
            record["embedding"] = np.array(eval(f.read()))
        record["title_rendered"] = wrap(record["title"], 10)
        records.append(record)
    return pd.DataFrame.from_records(records)


def transform(matrix: np.ndarray, perprexty=5) -> list[tuple[float, float]]:
    tsne = TSNE(
        n_components=2,
        perplexity=perprexty,
        random_state=12345,
        init="random",
        learning_rate=200,
    )
    return tsne.fit_transform(matrix)


def load_metadata(file: str) -> dict[str, str]:
    md_file = file[: -len(".encoded")]
    meta = dict()
    with open(md_file) as f:
        for line in f.readlines()[2:3]:
            key, value = line.split(":")
            meta[key] = value.strip()
    return meta


parser = budoux.load_default_japanese_parser()


def wrap(content: str, width: int) -> str:
    tokens = parser.parse(content)
    lines = []
    line = ""
    for token in tokens:
        line += token
        if len(line) >= width:
            lines.append(line)
            line = ""
    if line:
        lines.append(line)
    return "\n".join(lines)


def plot_data(vis_dims, labels):
    xs = [x for x, y in vis_dims]
    ys = [y for x, y in vis_dims]
    plt.scatter(xs, ys)
    for label, (x, y) in zip(labels, vis_dims):
        plt.annotate(
            wrap(label, 15),
            (x, y),
            textcoords="offset points",
            xytext=(0, 10),
            ha="center",
        )


def prompt(df: pd.DataFrame) -> tuple[str, np.ndarray]:
    while True:
        command = input("Command[search,recommend]")
        if "search".startswith(command):
            query = input("Query:")
            embedding = get_embedding(query, "text-embedding-ada-002")
            return [query, embedding]
        elif "recommend".startswith(command):
            index = input("Article Index:")
            embedding = df.embedding[int(index)]
            title = df.title[int(index)]
            return [title, embedding]


def visualize(files: list[str]):
    df = dataframe(files)
    matrix = np.array(df.embedding.to_list())
    print("shape", matrix.shape)
    print(df.title)

    with plt.ion():
        queries = []
        while True:
            query, embedding = prompt(df)
            plt.figure("plot")
            plt.cla()
            plt.title("Qiita articles")
            print("transform")
            vis_dims = transform(np.vstack([matrix, embedding]))
            vis_dims, query_dims = np.vsplit(vis_dims, [len(files)])

            vis_labels = [load_metadata(filename)["title"] for filename in files]

            print("prot data")
            plot_data(vis_dims, vis_labels)
            plot_data(query_dims, [query])

            plt.figure("ranking")
            plt.cla()
            df["similarity"] = df.embedding.apply(lambda x: np.dot(x, embedding))
            ranking = df.sort_values("similarity", ascending=False)
            print(ranking.filter(items=["similarity", "title"]))
            ranking = ranking.head(10)
            plt.bar(ranking.title_rendered, ranking.similarity)
            plt.xticks(ranking.title_rendered, ranking.title_rendered, rotation=60)
            plt.title("Similarity Ranking")
            plt.subplots_adjust(bottom=0.5)


if __name__ == "__main__":
    visualize(sys.argv[1:])
