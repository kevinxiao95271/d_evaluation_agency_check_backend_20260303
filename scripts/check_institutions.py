import requests

r = requests.get('http://localhost:5031/api/institution/list', timeout=5)
data = r.json()
print(f'机构数量: {len(data["data"])}')
