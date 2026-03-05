import requests

r = requests.get('http://localhost:5031/api/judge/list')
judges = r.json()['data']

expert_judges = [j for j in judges if j.get('type') == 'EXPERT']

print("专家评委名字列表 (前10个):")
for j in expert_judges[:10]:
    print(f"  - {j['name']}")
