from google import genai

client = genai.Client(api_key="AIzaSyBL0rMvtS4FeogdVOtxKSXa76aaQ69vqNw")

models = client.models.list()

for model in models:
    print(model.name)
