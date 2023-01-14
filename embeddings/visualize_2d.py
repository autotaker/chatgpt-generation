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
from openai.embeddings_utils import get_embedding

openai.api_key = os.getenv("OPENAI_API_KEY")


def dataframe(files: list[str]) -> np.ndarray:
    matrix = []
    for file in files:
        with open(file) as f:
            matrix.append(eval(f.read()))
    return np.array(matrix)


def transform(matrix: list[list[float]]) -> list[tuple[float, float]]:
    tsne = TSNE(
        n_components=2,
        perplexity=15,
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


def visualize(files: list[str]):
    matrix = dataframe(files)

    with plt.ion():
        queries = []
        while True:
            query = input()
            queries.append(query)
            plt.figure()
            embedding = get_embedding(query, "text-embedding-ada-002")

            matrix = np.vstack([matrix, embedding])
            vis_dims = transform(matrix)
            vis_dims, query_dims = np.vsplit(vis_dims, [len(files)])

            vis_labels = [load_metadata(filename)["title"] for filename in files]
            plot_data(vis_dims, vis_labels)
            plot_data(query_dims, queries)
            plt.title("Qiita articles")


if __name__ == "__main__":
    visualize(sys.argv[1:])
