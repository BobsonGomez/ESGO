from google import genai

client = genai.Client(api_key="AIzaSyCV5JsLr-j96Z5hGEZD_R-xoN0v0ZF0NjE")

models = client.models.list()

for model in models:
    print(model.name)
