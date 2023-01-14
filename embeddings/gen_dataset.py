import os
from typing import Generator
import tiktoken
import requests

API_TOKEN = os.getenv("QIITA_API_TOKEN")
API_ENDPOINT_URL = "https://qiita.com/api/v2"


def list_items() -> Generator[dict, None, None]:
    headers = {"Authorization": f"Bearer {API_TOKEN}"}
    URL = API_ENDPOINT_URL + "/authenticated_user/items"
    page = 1
    items = []
    print("headers", headers)
    while True:
        params = {"page": str(page)}
        res = requests.get(URL, params=params, headers=headers)
        page_items = res.json()
        for item in page_items:
            yield item
        print("len(page_items): ", len(page_items))
        if len(page_items) < 20:
            break
        page += 1


def gen_dataset():
    for item in list_items():
        body = item["body"]
        id = item["id"]
        tags = " ".join(tag["name"] for tag in item["tags"])
        title = item["title"]
        metadata = "\n".join(["---", "tags: " + tags, "title: " + title, "---"]) + "\n"
        if item["private"]:
            print("skip: ", title)
            continue
        print("title: ", title)
        with open(f"data/{id}.md", "w") as f:
            f.write(metadata)
            f.write(body)


if __name__ == "__main__":
    gen_dataset()
