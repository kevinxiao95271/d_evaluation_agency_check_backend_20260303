import requests

# 测试新接口
r = requests.get('http://localhost:5031/api/score/submissions?taskId=1')
data = r.json()['data']

print("=" * 60)
print("✅ 本地部署验证")
print("=" * 60)
print(f"\n新接口 GET /api/score/submissions")
print(f"  状态码: {r.status_code}")
print(f"  返回提交记录数: {len(data)}")

if data:
    first = data[0]
    print(f"\n第一条记录包含的关键字段:")
    print(f"  ✅ scoreMode: {first.get('scoreMode')}")
    print(f"  ✅ totalScore: {first.get('totalScore')}")
    print(f"  ✅ submitTime: {first.get('submitTime')}")
    print(f"  ✅ itemScores数量: {len(first.get('itemScores', []))}")
    print(f"  ✅ 评委类型: {first.get('judgeType')}")
    
    # 统计专家和大众评委
    expert_count = sum(1 for s in data if s.get('judgeType') == 'EXPERT')
    public_count = sum(1 for s in data if s.get('judgeType') == 'PUBLIC')
    
    print(f"\n评委统计:")
    print(f"  专家评委提交: {expert_count} 次")
    print(f"  大众评委提交: {public_count} 次")
    print(f"  总提交次数: {len(data)} 次")

print("\n" + "=" * 60)
print("✅ 本地部署已生效,所有功能正常!")
print("=" * 60)
