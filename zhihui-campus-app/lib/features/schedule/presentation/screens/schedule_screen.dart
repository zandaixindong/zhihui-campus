import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';

class ScheduleScreen extends ConsumerStatefulWidget {
  const ScheduleScreen({super.key});

  @override
  ConsumerState<ScheduleScreen> createState() => _ScheduleScreenState();
}

class _ScheduleScreenState extends ConsumerState<ScheduleScreen> {
  int _currentWeek = 15;
  final int _totalWeeks = 20;

  // 示例课程数据
  final List<Map<String, dynamic>> _courses = [
    {
      'name': '高等数学',
      'teacher': '张老师',
      'room': 'A101',
      'day': 1,
      'startSection': 1,
      'endSection': 2,
      'color': Colors.blue,
    },
    {
      'name': '大学英语',
      'teacher': '李老师',
      'room': 'B203',
      'day': 2,
      'startSection': 3,
      'endSection': 4,
      'color': Colors.green,
    },
    {
      'name': '程序设计',
      'teacher': '王老师',
      'room': 'C305',
      'day': 3,
      'startSection': 5,
      'endSection': 6,
      'color': Colors.orange,
    },
    {
      'name': '数据结构',
      'teacher': '赵老师',
      'room': 'D401',
      'day': 4,
      'startSection': 1,
      'endSection': 2,
      'color': Colors.purple,
    },
    {
      'name': '计算机网络',
      'teacher': '刘老师',
      'room': 'E201',
      'day': 5,
      'startSection': 3,
      'endSection': 4,
      'color': Colors.red,
    },
  ];

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('我的课表'),
        actions: [
          IconButton(
            icon: const Icon(Icons.calendar_today),
            onPressed: _showWeekPicker,
          ),
        ],
      ),
      body: Column(
        children: [
          // 周次选择
          _buildWeekSelector(),

          // 课表
          Expanded(
            child: _buildScheduleGrid(),
          ),
        ],
      ),
    );
  }

  Widget _buildWeekSelector() {
    return Container(
      padding: const EdgeInsets.symmetric(vertical: 12, horizontal: 16),
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
      child: Row(
        mainAxisAlignment: MainAxisAlignment.spaceBetween,
        children: [
          IconButton(
            icon: const Icon(Icons.chevron_left),
            onPressed: _currentWeek > 1
                ? () => setState(() => _currentWeek--)
                : null,
          ),
          GestureDetector(
            onTap: _showWeekPicker,
            child: Row(
              children: [
                Text(
                  '第 $_currentWeek 周',
                  style: const TextStyle(
                    fontSize: 18,
                    fontWeight: FontWeight.bold,
                  ),
                ),
                const Icon(Icons.arrow_drop_down),
              ],
            ),
          ),
          IconButton(
            icon: const Icon(Icons.chevron_right),
            onPressed: _currentWeek < _totalWeeks
                ? () => setState(() => _currentWeek++)
                : null,
          ),
        ],
      ),
    );
  }

  Widget _buildScheduleGrid() {
    const days = ['周一', '周二', '周三', '周四', '周五', '周六', '周日'];
    const sections = [
      '第1节',
      '第2节',
      '第3节',
      '第4节',
      '第5节',
      '第6节',
      '第7节',
      '第8节',
      '第9节',
      '第10节',
    ];

    return SingleChildScrollView(
      child: Row(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          // 节次列
          SizedBox(
            width: 40,
            child: Column(
              children: [
                const SizedBox(height: 40), // 表头高度
                ...sections.map((section) => Container(
                  height: 60,
                  alignment: Alignment.center,
                  child: Text(
                    section,
                    style: const TextStyle(fontSize: 10, color: Colors.grey),
                  ),
                )),
              ],
            ),
          ),

          // 课表网格
          Expanded(
            child: Column(
              children: [
                // 星期表头
                Row(
                  children: days.map((day) => Expanded(
                    child: Container(
                      height: 40,
                      alignment: Alignment.center,
                      decoration: BoxDecoration(
                        color: Colors.grey[100],
                        border: Border(
                          bottom: BorderSide(color: Colors.grey[300]!),
                        ),
                      ),
                      child: Text(
                        day,
                        style: const TextStyle(
                          fontWeight: FontWeight.bold,
                          fontSize: 12,
                        ),
                      ),
                    ),
                  )).toList(),
                ),

                // 课表格子
                SizedBox(
                  height: 60 * 10,
                  child: Stack(
                    children: [
                      // 网格线
                      ...List.generate(10, (index) => Positioned(
                        top: index * 60.0,
                        left: 0,
                        right: 0,
                        child: Container(
                          height: 0.5,
                          color: Colors.grey[300],
                        ),
                      )),

                      // 课程卡片
                      ..._courses.map((course) => _buildCourseCard(course)),
                    ],
                  ),
                ),
              ],
            ),
          ),
        ],
      ),
    );
  }

  Widget _buildCourseCard(Map<String, dynamic> course) {
    final day = course['day'] as int;
    final startSection = course['startSection'] as int;
    final endSection = course['endSection'] as int;
    final color = course['color'] as Color;

    final left = (day - 1) * (MediaQuery.of(context).size.width - 40) / 7;
    final top = (startSection - 1) * 60.0;
    final height = (endSection - startSection + 1) * 60.0 - 4;

    return Positioned(
      left: left + 2,
      top: top + 2,
      width: (MediaQuery.of(context).size.width - 40) / 7 - 4,
      height: height,
      child: GestureDetector(
        onTap: () => _showCourseDetail(course),
        child: Container(
          padding: const EdgeInsets.all(4),
          decoration: BoxDecoration(
            color: color.withOpacity(0.85),
            borderRadius: BorderRadius.circular(6),
          ),
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            children: [
              Text(
                course['name'],
                style: const TextStyle(
                  color: Colors.white,
                  fontSize: 11,
                  fontWeight: FontWeight.bold,
                ),
                textAlign: TextAlign.center,
                maxLines: 2,
                overflow: TextOverflow.ellipsis,
              ),
              const SizedBox(height: 2),
              Text(
                '@${course['room']}',
                style: const TextStyle(
                  color: Colors.white70,
                  fontSize: 9,
                ),
                textAlign: TextAlign.center,
              ),
            ],
          ),
        ),
      ),
    );
  }

  void _showWeekPicker() {
    showModalBottomSheet(
      context: context,
      builder: (context) {
        return Container(
          height: 300,
          padding: const EdgeInsets.all(16),
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              const Text(
                '选择周次',
                style: TextStyle(
                  fontSize: 18,
                  fontWeight: FontWeight.bold,
                ),
              ),
              const SizedBox(height: 16),
              Expanded(
                child: GridView.builder(
                  gridDelegate: const SliverGridDelegateWithFixedCrossAxisCount(
                    crossAxisCount: 5,
                    mainAxisSpacing: 8,
                    crossAxisSpacing: 8,
                  ),
                  itemCount: _totalWeeks,
                  itemBuilder: (context, index) {
                    final week = index + 1;
                    final isSelected = week == _currentWeek;
                    return GestureDetector(
                      onTap: () {
                        setState(() => _currentWeek = week);
                        Navigator.pop(context);
                      },
                      child: Container(
                        alignment: Alignment.center,
                        decoration: BoxDecoration(
                          color: isSelected
                              ? Theme.of(context).primaryColor
                              : Colors.grey[100],
                          borderRadius: BorderRadius.circular(8),
                        ),
                        child: Text(
                          '$week',
                          style: TextStyle(
                            color: isSelected ? Colors.white : Colors.black,
                            fontWeight: FontWeight.bold,
                          ),
                        ),
                      ),
                    );
                  },
                ),
              ),
            ],
          ),
        );
      },
    );
  }

  void _showCourseDetail(Map<String, dynamic> course) {
    showModalBottomSheet(
      context: context,
      builder: (context) {
        return Container(
          padding: const EdgeInsets.all(24),
          child: Column(
            mainAxisSize: MainAxisSize.min,
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              Text(
                course['name'],
                style: const TextStyle(
                  fontSize: 20,
                  fontWeight: FontWeight.bold,
                ),
              ),
              const SizedBox(height: 16),
              _buildDetailRow(Icons.person, '教师', course['teacher']),
              _buildDetailRow(Icons.location_on, '教室', course['room']),
              _buildDetailRow(
                Icons.access_time,
                '时间',
                '周${_getDayName(course['day'])} 第${course['startSection']}-${course['endSection']}节',
              ),
              const SizedBox(height: 24),
              SizedBox(
                width: double.infinity,
                child: ElevatedButton(
                  onPressed: () => Navigator.pop(context),
                  child: const Text('关闭'),
                ),
              ),
            ],
          ),
        );
      },
    );
  }

  Widget _buildDetailRow(IconData icon, String label, String value) {
    return Padding(
      padding: const EdgeInsets.symmetric(vertical: 8),
      child: Row(
        children: [
          Icon(icon, size: 20, color: Colors.grey),
          const SizedBox(width: 12),
          Text(
            '$label：',
            style: const TextStyle(color: Colors.grey),
          ),
          Text(value),
        ],
      ),
    );
  }

  String _getDayName(int day) {
    const days = ['一', '二', '三', '四', '五', '六', '日'];
    return days[day - 1];
  }
}
