import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';

class ScoreScreen extends ConsumerStatefulWidget {
  const ScoreScreen({super.key});

  @override
  ConsumerState<ScoreScreen> createState() => _ScoreScreenState();
}

class _ScoreScreenState extends ConsumerState<ScoreScreen> {
  String _selectedSemester = '2024-2025-2';
  final List<String> _semesters = [
    '2024-2025-2',
    '2024-2025-1',
    '2023-2024-2',
    '2023-2024-1',
  ];

  // 示例成绩数据
  final List<Map<String, dynamic>> _scores = [
    {
      'courseName': '高等数学A',
      'courseCode': 'MATH1001',
      'credits': 5.0,
      'score': 92.0,
      'gpa': 4.2,
      'grade': '优秀',
    },
    {
      'courseName': '大学英语(二)',
      'courseCode': 'ENG1002',
      'credits': 4.0,
      'score': 85.0,
      'gpa': 3.7,
      'grade': '良好',
    },
    {
      'courseName': '程序设计基础',
      'courseCode': 'CS1001',
      'credits': 3.0,
      'score': 95.0,
      'gpa': 4.5,
      'grade': '优秀',
    },
    {
      'courseName': '数据结构',
      'courseCode': 'CS2001',
      'credits': 4.0,
      'score': 88.0,
      'gpa': 3.9,
      'grade': '良好',
    },
    {
      'courseName': '计算机网络',
      'courseCode': 'CS2002',
      'credits': 3.0,
      'score': 78.0,
      'gpa': 3.2,
      'grade': '中等',
    },
    {
      'courseName': '线性代数',
      'courseCode': 'MATH2001',
      'credits': 3.0,
      'score': 82.0,
      'gpa': 3.5,
      'grade': '良好',
    },
  ];

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('成绩查询'),
      ),
      body: Column(
        children: [
          // 学期选择和统计
          _buildHeader(),

          // 成绩列表
          Expanded(
            child: _buildScoreList(),
          ),
        ],
      ),
    );
  }

  Widget _buildHeader() {
    // 计算统计数据
    double totalCredits = 0;
    double totalWeightedScore = 0;
    double totalWeightedGpa = 0;

    for (var score in _scores) {
      final credits = score['credits'] as double;
      final scoreValue = score['score'] as double;
      final gpa = score['gpa'] as double;

      totalCredits += credits;
      totalWeightedScore += credits * scoreValue;
      totalWeightedGpa += credits * gpa;
    }

    final avgScore = totalCredits > 0 ? totalWeightedScore / totalCredits : 0;
    final avgGpa = totalCredits > 0 ? totalWeightedGpa / totalCredits : 0;

    return Container(
      padding: const EdgeInsets.all(16),
      decoration: BoxDecoration(
        color: Colors.white,
        boxShadow: [
          BoxShadow(
            color: Colors.grey.withOpacity(0.1),
            spreadRadius: 1,
            blurRadius: 5,
            offset: const Offset(0, 2),
          ),
        ],
      ),
      child: Column(
        children: [
          // 学期选择
          Row(
            mainAxisAlignment: MainAxisAlignment.spaceBetween,
            children: [
              const Text(
                '当前学期',
                style: TextStyle(
                  fontSize: 16,
                  fontWeight: FontWeight.bold,
                ),
              ),
              DropdownButton<String>(
                value: _selectedSemester,
                items: _semesters.map((semester) {
                  return DropdownMenuItem(
                    value: semester,
                    child: Text(semester),
                  );
                }).toList(),
                onChanged: (value) {
                  setState(() {
                    _selectedSemester = value!;
                  });
                },
              ),
            ],
          ),
          const SizedBox(height: 16),

          // 统计卡片
          Row(
            children: [
              _buildStatCard('总学分', totalCredits.toStringAsFixed(1), Colors.blue),
              _buildStatCard('平均分', avgScore.toStringAsFixed(1), Colors.green),
              _buildStatCard('平均绩点', avgGpa.toStringAsFixed(2), Colors.orange),
            ],
          ),
        ],
      ),
    );
  }

  Widget _buildStatCard(String label, String value, Color color) {
    return Expanded(
      child: Container(
        margin: const EdgeInsets.symmetric(horizontal: 4),
        padding: const EdgeInsets.all(12),
        decoration: BoxDecoration(
          color: color.withOpacity(0.1),
          borderRadius: BorderRadius.circular(8),
        ),
        child: Column(
          children: [
            Text(
              value,
              style: TextStyle(
                fontSize: 24,
                fontWeight: FontWeight.bold,
                color: color,
              ),
            ),
            const SizedBox(height: 4),
            Text(
              label,
              style: TextStyle(
                fontSize: 12,
                color: color.withOpacity(0.8),
              ),
            ),
          ],
        ),
      ),
    );
  }

  Widget _buildScoreList() {
    return ListView.builder(
      padding: const EdgeInsets.all(16),
      itemCount: _scores.length,
      itemBuilder: (context, index) {
        final score = _scores[index];
        return _buildScoreItem(score);
      },
    );
  }

  Widget _buildScoreItem(Map<String, dynamic> score) {
    final scoreValue = score['score'] as double;
    Color scoreColor;

    if (scoreValue >= 90) {
      scoreColor = Colors.green;
    } else if (scoreValue >= 80) {
      scoreColor = Colors.blue;
    } else if (scoreValue >= 70) {
      scoreColor = Colors.orange;
    } else if (scoreValue >= 60) {
      scoreColor = Colors.yellow[700]!;
    } else {
      scoreColor = Colors.red;
    }

    return Card(
      margin: const EdgeInsets.only(bottom: 12),
      child: Padding(
        padding: const EdgeInsets.all(16),
        child: Row(
          children: [
            // 成绩分数
            Container(
              width: 60,
              height: 60,
              decoration: BoxDecoration(
                color: scoreColor.withOpacity(0.1),
                borderRadius: BorderRadius.circular(12),
              ),
              child: Column(
                mainAxisAlignment: MainAxisAlignment.center,
                children: [
                  Text(
                    scoreValue.toInt().toString(),
                    style: TextStyle(
                      fontSize: 24,
                      fontWeight: FontWeight.bold,
                      color: scoreColor,
                    ),
                  ),
                  Text(
                    score['grade'],
                    style: TextStyle(
                      fontSize: 10,
                      color: scoreColor,
                    ),
                  ),
                ],
              ),
            ),
            const SizedBox(width: 16),

            // 课程信息
            Expanded(
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Text(
                    score['courseName'],
                    style: const TextStyle(
                      fontSize: 16,
                      fontWeight: FontWeight.bold,
                    ),
                  ),
                  const SizedBox(height: 4),
                  Text(
                    '${score['courseCode']} · ${score['credits']}学分',
                    style: const TextStyle(
                      fontSize: 14,
                      color: Colors.grey,
                    ),
                  ),
                ],
              ),
            ),

            // 绩点
            Column(
              crossAxisAlignment: CrossAxisAlignment.end,
              children: [
                const Text(
                  '绩点',
                  style: TextStyle(
                    fontSize: 12,
                    color: Colors.grey,
                  ),
                ),
                Text(
                  score['gpa'].toString(),
                  style: TextStyle(
                    fontSize: 18,
                    fontWeight: FontWeight.bold,
                    color: scoreColor,
                  ),
                ),
              ],
            ),
          ],
        ),
      ),
    );
  }
}
