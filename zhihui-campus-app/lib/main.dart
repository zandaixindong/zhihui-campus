import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:hive_flutter/hive_flutter.dart';
import 'package:flutter_easyloading/flutter_easyloading.dart';

import 'app/routes/app_router.dart';
import 'shared/theme/app_theme.dart';
import 'core/storage/local_storage.dart';

void main() async {
  WidgetsFlutterBinding.ensureInitialized();

  // 初始化本地存储
  await Hive.initFlutter();
  await LocalStorage.init();

  runApp(
    const ProviderScope(
      child: ZhihuiCampusApp(),
    ),
  );
}

class ZhihuiCampusApp extends ConsumerWidget {
  const ZhihuiCampusApp({super.key});

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final router = ref.watch(appRouterProvider);

    return MaterialApp.router(
      title: '智慧校园',
      debugShowCheckedModeBanner: false,
      theme: AppTheme.lightTheme,
      darkTheme: AppTheme.darkTheme,
      themeMode: ThemeMode.light,
      routerConfig: router,
      builder: EasyLoading.init(),
    );
  }
}
