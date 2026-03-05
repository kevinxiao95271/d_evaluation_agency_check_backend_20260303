import requests

print("=" * 60)
print("验证专家评委数据")
print("=" * 60)

# 1. 检查评委数据
r = requests.get('http://localhost:5031/api/judge/list')
judges = r.json()['data']

expert_judges = [j for j in judges if j.get('type') == 'EXPERT']
public_judges = [j for j in judges if j.get('type') == 'PUBLIC']

print(f"\n1. 评委数据:")
print(f"   专家评委数: {len(expert_judges)}")
print(f"   大众评委数: {len(public_judges)}")

# 检查专家评委是否有机构信息
experts_with_institution = [j for j in expert_judges if j.get('institutionId')]
print(f"\n2. 专家评委机构信息:")
print(f"   有机构信息的专家: {len(experts_with_institution)}")
print(f"   无机构信息的专家: {len(expert_judges) - len(experts_with_institution)}")
if len(experts_with_institution) == 0:
    print(f"   ✅ 正确: 所有专家评委都没有机构信息")
else:
    print(f"   ❌ 错误: 还有专家评委关联了机构")

# 3. 检查评分记录
r = requests.get('http://localhost:5031/api/score/submissions?taskId=1')
submissions = r.json()['data']

expert_submissions = [s for s in submissions if s.get('judgeType') == 'EXPERT']
public_submissions = [s for s in submissions if s.get('judgeType') == 'PUBLIC']

print(f"\n3. 评分提交记录:")
print(f"   专家评委提交: {len(expert_submissions)} 次")
print(f"   大众评委提交: {len(public_submissions)} 次")

# 检查专家评委是否评审了所有机构
institutions = set(s['institutionId'] for s in expert_submissions)
print(f"\n4. 专家评委评审范围:")
print(f"   评审的机构数: {len(institutions)}")
print(f"   预期机构数: 10")
if len(institutions) == 10:
    print(f"   ✅ 正确: 专家评委评审了所有10个机构")
else:
    print(f"   ❌ 错误: 专家评委未评审所有机构")

# 检查大众评委的同机构回避
print(f"\n5. 大众评委同机构回避验证:")
# 获取机构1的评分
r = requests.get('http://localhost:5031/api/score/submissions?institutionId=1')
inst1_submissions = r.json()['data']

public_for_inst1 = [s for s in inst1_submissions if s.get('judgeType') == 'PUBLIC']
print(f"   机构1收到的大众评委评分: {len(public_for_inst1)} 次")
print(f"   预期: 4次 (5个大众评委 - 1个同机构回避)")

# 检查是否有"临床检验中心代表"评审机构1
has_same_inst = any(s['judgeName'] == '临床检验中心代表' for s in inst1_submissions)
if not has_same_inst:
    print(f"   ✅ 正确: 临床检验中心代表没有评审临床检验中心(同机构回避)")
else:
    print(f"   ❌ 错误: 同机构回避未生效")

print("\n" + "=" * 60)
print("✅ 验证完成!")
print("=" * 60)
