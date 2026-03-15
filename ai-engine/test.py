from google import genai

client = genai.Client(api_key="AIzaSyB9eW-boj9ZHx_EGydN9KeTOfJmqbfML4w")

models = client.models.list()

for model in models:
    print(model.name)
