import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:go_router/go_router.dart';

import '../../../../core/storage/local_storage.dart';

class ProfileScreen extends ConsumerStatefulWidget {
  const ProfileScreen({super.key});

  @override
  ConsumerState<ProfileScreen> createState() => _ProfileScreenState();
}

class _ProfileScreenState extends ConsumerState<ProfileScreen> {
  Map<String, dynamic>? _userInfo;

  @override
  void initState() {
    super.initState();
    _loadUserInfo();
  }

  void _loadUserInfo() {
    setState(() {
      _userInfo = LocalStorage.getUserInfo();
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('个人中心'),
        actions: [
          IconButton(
            icon: const Icon(Icons.settings),
            onPressed: () {
              // TODO: 跳转设置页面
            },
          ),
        ],
      ),
      body: SingleChildScrollView(
        child: Column(
          children: [
            // 用户信息卡片
            _buildUserInfoCard(),
            const SizedBox(height: 12),

            // 功能列表
            _buildFunctionList(),
            const SizedBox(height: 12),

            // 退出登录
            _buildLogoutButton(),
            const SizedBox(height: 32),
          ],
        ),
      ),
    );
  }

  Widget _buildUserInfoCard() {
    return Container(
      margin: const EdgeInsets.all(16),
      padding: const EdgeInsets.all(20),
      decoration: BoxDecoration(
        gradient: const LinearGradient(
          colors: [Color(0xFF667eea), Color(0xFF764ba2)],
          begin: Alignment.topLeft,
          end: Alignment.bottomRight,
        ),
        borderRadius: BorderRadius.circular(16),
        boxShadow: [
          BoxShadow(
            color: const Color(0xFF667eea).withOpacity(0.3),
            blurRadius: 15,
            offset: const Offset(0, 8),
          ),
        ],
      ),
      child: Row(
        children: [
          // 头像
          Container(
            width: 70,
            height: 70,
            decoration: BoxDecoration(
              color: Colors.white,
              borderRadius: BorderRadius.circular(35),
              boxShadow: [
                BoxShadow(
                  color: Colors.black.withOpacity(0.1),
                  blurRadius: 10,
                  offset: const Offset(0, 4),
                ),
              ],
            ),
            child: _userInfo?['avatar'] != null
                ? ClipRRect(
                    borderRadius: BorderRadius.circular(35),
                    child: Image.network(
                      _userInfo!['avatar'],
                      fit: BoxFit.cover,
                      errorBuilder: (context, error, stackTrace) {
                        return const Icon(
                          Icons.person,
                          size: 40,
                          color: Color(0xFF667eea),
                        );
                      },
                    ),
                  )
                : const Icon(
                    Icons.person,
                    size: 40,
                    color: Color(0xFF667eea),
                  ),
          ),
          const SizedBox(width: 16),

          // 用户信息
          Expanded(
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                Text(
                  _userInfo?['realName'] ?? _userInfo?['username'] ?? '未登录',
                  style: const TextStyle(
                    fontSize: 20,
                    fontWeight: FontWeight.bold,
                    color: Colors.white,
                  ),
                ),
                const SizedBox(height: 4),
                Text(
                  _getUserTypeText(_userInfo?['userType']),
                  style: const TextStyle(
                    fontSize: 14,
                    color: Colors.white70,
                  ),
                ),
                if (_userInfo?['username'] != null) ...[
                  const SizedBox(height: 4),
                  Text(
                    '学号：${_userInfo!['username']}',
                    style: const TextStyle(
                      fontSize: 14,
                      color: Colors.white70,
                    ),
                  ),
                ],
              ],
            ),
          ),

          // 编辑按钮
          IconButton(
            icon: const Icon(Icons.edit, color: Colors.white),
            onPressed: () {
              // TODO: 编辑个人信息
            },
          ),
        ],
      ),
    );
  }

  Widget _buildFunctionList() {
    final functions = [
      {
        'title': '我的课程',
        'icon': Icons.book,
        'color': Colors.blue,
        'onTap': () {},
      },
      {
        'title': '我的成绩',
        'icon': Icons.description,
        'color': Colors.green,
        'onTap': () => context.go('/score'),
      },
      {
        'title': '我的收藏',
        'icon': Icons.star,
        'color': Colors.orange,
        'onTap': () {},
      },
      {
        'title': '我的评论',
        'icon': Icons.comment,
        'color': Colors.purple,
        'onTap': () {},
      },
      {
        'title': '浏览历史',
        'icon': Icons.history,
        'color': Colors.teal,
        'onTap': () {},
      },
    ];

    final settings = [
      {
        'title': '消息通知',
        'icon': Icons.notifications,
        'color': Colors.red,
        'onTap': () {},
      },
      {
        'title': '隐私设置',
        'icon': Icons.privacy_tip,
        'color': Colors.indigo,
        'onTap': () {},
      },
      {
        'title': '清除缓存',
        'icon': Icons.cleaning_services,
        'color': Colors.grey,
        'onTap': _showClearCacheDialog,
      },
      {
        'title': '关于我们',
        'icon': Icons.info,
        'color': Colors.cyan,
        'onTap': _showAboutDialog,
      },
      {
        'title': '帮助与反馈',
        'icon': Icons.help,
        'color': Colors.amber,
        'onTap': () {},
      },
    ];

    return Column(
      children: [
        // 我的功能
        Container(
          margin: const EdgeInsets.symmetric(horizontal: 16),
          decoration: BoxDecoration(
            color: Colors.white,
            borderRadius: BorderRadius.circular(12),
          ),
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              const Padding(
                padding: EdgeInsets.fromLTRB(16, 16, 16, 8),
                child: Text(
                  '我的功能',
                  style: TextStyle(
                    fontSize: 16,
                    fontWeight: FontWeight.bold,
                  ),
                ),
              ),
              ...functions.map((func) => _buildFunctionItem(func)),
            ],
          ),
        ),
        const SizedBox(height: 12),

        // 设置
        Container(
          margin: const EdgeInsets.symmetric(horizontal: 16),
          decoration: BoxDecoration(
            color: Colors.white,
            borderRadius: BorderRadius.circular(12),
          ),
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              const Padding(
                padding: EdgeInsets.fromLTRB(16, 16, 16, 8),
                child: Text(
                  '设置',
                  style: TextStyle(
                    fontSize: 16,
                    fontWeight: FontWeight.bold,
                  ),
                ),
              ),
              ...settings.map((setting) => _buildFunctionItem(setting)),
            ],
          ),
        ),
      ],
    );
  }

  Widget _buildFunctionItem(Map<String, dynamic> item) {
    return ListTile(
      leading: Container(
        width: 40,
        height: 40,
        decoration: BoxDecoration(
          color: (item['color'] as Color).withOpacity(0.1),
          borderRadius: BorderRadius.circular(10),
        ),
        child: Icon(
          item['icon'] as IconData,
          color: item['color'] as Color,
          size: 22,
        ),
      ),
      title: Text(item['title'] as String),
      trailing: const Icon(Icons.chevron_right, color: Colors.grey),
      onTap: item['onTap'] as VoidCallback,
    );
  }

  Widget _buildLogoutButton() {
    return Container(
      margin: const EdgeInsets.symmetric(horizontal: 16),
      width: double.infinity,
      child: ElevatedButton(
        onPressed: _showLogoutDialog,
        style: ElevatedButton.styleFrom(
          backgroundColor: Colors.red[50],
          foregroundColor: Colors.red,
          padding: const EdgeInsets.symmetric(vertical: 16),
          shape: RoundedRectangleBorder(
            borderRadius: BorderRadius.circular(12),
          ),
        ),
        child: const Text(
          '退出登录',
          style: TextStyle(
            fontSize: 16,
            fontWeight: FontWeight.bold,
          ),
        ),
      ),
    );
  }

  String _getUserTypeText(int? userType) {
    switch (userType) {
      case 1:
        return '学生';
      case 2:
        return '教师';
      case 3:
        return '管理员';
      case 4:
        return '校友';
      default:
        return '未知身份';
    }
  }

  void _showLogoutDialog() {
    showDialog(
      context: context,
      builder: (context) {
        return AlertDialog(
          title: const Text('退出登录'),
          content: const Text('确定要退出登录吗？'),
          actions: [
            TextButton(
              onPressed: () => Navigator.pop(context),
              child: const Text('取消'),
            ),
            ElevatedButton(
              onPressed: () {
                Navigator.pop(context);
                _logout();
              },
              style: ElevatedButton.styleFrom(
                backgroundColor: Colors.red,
                foregroundColor: Colors.white,
              ),
              child: const Text('确定'),
            ),
          ],
        );
      },
    );
  }

  void _logout() {
    LocalStorage.clearToken();
    context.go('/login');
  }

  void _showClearCacheDialog() {
    showDialog(
      context: context,
      builder: (context) {
        return AlertDialog(
          title: const Text('清除缓存'),
          content: const Text('确定要清除本地缓存吗？'),
          actions: [
            TextButton(
              onPressed: () => Navigator.pop(context),
              child: const Text('取消'),
            ),
            ElevatedButton(
              onPressed: () {
                Navigator.pop(context);
                ScaffoldMessenger.of(context).showSnackBar(
                  const SnackBar(content: Text('缓存已清除')),
                );
              },
              child: const Text('确定'),
            ),
          ],
        );
      },
    );
  }

  void _showAboutDialog() {
    showAboutDialog(
      context: context,
      applicationName: '智慧校园',
      applicationVersion: '1.0.0',
      applicationIcon: Container(
        width: 60,
        height: 60,
        decoration: BoxDecoration(
          color: const Color(0xFF667eea),
          borderRadius: BorderRadius.circular(15),
        ),
        child: const Icon(
          Icons.school,
          color: Colors.white,
          size: 36,
        ),
      ),
      children: const [
        Text('智慧校园服务平台为师生提供一站式校园服务，包括教务查询、生活服务、通知公告等功能。'),
      ],
    );
  }
}
