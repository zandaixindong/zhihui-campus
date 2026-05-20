import 'package:hive_flutter/hive_flutter.dart';
import 'package:shared_preferences/shared_preferences.dart';

class LocalStorage {
  static late SharedPreferences _prefs;
  static late Box _box;

  static const String _tokenKey = 'token';
  static const String _refreshTokenKey = 'refresh_token';
  static const String _userInfoKey = 'user_info';
  static const String _themeKey = 'theme_mode';
  static const String _localeKey = 'locale';

  /// 初始化
  static Future<void> init() async {
    _prefs = await SharedPreferences.getInstance();
    _box = await Hive.openBox('app_storage');
  }

  // ==================== Token相关 ====================

  /// 获取Token
  static String? getToken() {
    return _prefs.getString(_tokenKey);
  }

  /// 设置Token
  static Future<bool> setToken(String token) {
    return _prefs.setString(_tokenKey, token);
  }

  /// 获取刷新Token
  static String? getRefreshToken() {
    return _prefs.getString(_refreshTokenKey);
  }

  /// 设置刷新Token
  static Future<bool> setRefreshToken(String token) {
    return _prefs.setString(_refreshTokenKey, token);
  }

  /// 清除Token
  static Future<void> clearToken() async {
    await _prefs.remove(_tokenKey);
    await _prefs.remove(_refreshTokenKey);
  }

  // ==================== 用户信息 ====================

  /// 获取用户信息
  static Map<String, dynamic>? getUserInfo() {
    final userInfoStr = _box.get(_userInfoKey);
    if (userInfoStr != null) {
      return Map<String, dynamic>.from(userInfoStr);
    }
    return null;
  }

  /// 设置用户信息
  Future<void> setUserInfo(Map<String, dynamic> userInfo) async {
    await _box.put(_userInfoKey, userInfo);
  }

  /// 清除用户信息
  Future<void> clearUserInfo() async {
    await _box.delete(_userInfoKey);
  }

  // ==================== 主题设置 ====================

  /// 获取主题模式
  static String getThemeMode() {
    return _prefs.getString(_themeKey) ?? 'light';
  }

  /// 设置主题模式
  static Future<bool> setThemeMode(String mode) {
    return _prefs.setString(_themeKey, mode);
  }

  // ==================== 语言设置 ====================

  /// 获取语言
  static String getLocale() {
    return _prefs.getString(_localeKey) ?? 'zh_CN';
  }

  /// 设置语言
  static Future<bool> setLocale(String locale) {
    return _prefs.setString(_localeKey, locale);
  }

  // ==================== 通用方法 ====================

  /// 获取字符串
  static String? getString(String key) {
    return _prefs.getString(key);
  }

  /// 设置字符串
  static Future<bool> setString(String key, String value) {
    return _prefs.setString(key, value);
  }

  /// 获取整数
  static int? getInt(String key) {
    return _prefs.getInt(key);
  }

  /// 设置整数
  static Future<bool> setInt(String key, int value) {
    return _prefs.setInt(key, value);
  }

  /// 获取布尔
  static bool? getBool(String key) {
    return _prefs.getBool(key);
  }

  /// 设置布尔
  static Future<bool> setBool(String key, bool value) {
    return _prefs.setBool(key, value);
  }

  /// 删除指定key
  static Future<bool> remove(String key) {
    return _prefs.remove(key);
  }

  /// 清除所有数据
  static Future<void> clearAll() async {
    await _prefs.clear();
    await _box.clear();
  }

  /// 是否已登录
  static bool isLoggedIn() {
    return getToken() != null;
  }
}
