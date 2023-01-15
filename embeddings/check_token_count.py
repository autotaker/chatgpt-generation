import tiktoken
import sys

enc = tiktoken.get_encoding("gpt2")


def check_token_count(file: str) -> int:
    with open(file) as f:
        content = f.read()
        tokens = enc.encode(content)
        return len(tokens)


if __name__ == "__main__":
    for file in sys.argv[1:]:
        count = check_token_count(file)
        if count <= 8192:
            print(f"{file}: {count}")
        else:
            print(f"{file}: {count} Too Long!")
