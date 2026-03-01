from google import genai

client = genai.Client(api_key="AIzaSyCzU-8hDSudUa7nWywreGnxCmz2OwxZCTY")

models = client.models.list()

for model in models:
    print(model.name)
