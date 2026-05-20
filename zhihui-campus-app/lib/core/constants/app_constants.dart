class AppConstants {
  // API基础地址
  static const String baseUrl = 'http://localhost:8080';

  // 应用名称
  static const String appName = '智慧校园';

  // 应用版本
  static const String appVersion = '1.0.0';

  // 分页大小
  static const int pageSize = 20;

  // 缓存时间（毫秒）
  static const int cacheDuration = 5 * 60 * 1000; // 5分钟

  // Token过期时间（毫秒）
  static const int tokenExpireDuration = 720 * 60 * 1000; // 12小时

  // 图片质量
  static const int imageQuality = 80;

  // 最大上传文件大小（MB）
  static const int maxUploadFileSize = 10;

  // 支持的图片格式
  static const List<String> supportedImageFormats = [
    'jpg',
    'jpeg',
    'png',
    'gif',
    'webp',
  ];

  // 用户类型
  static const int userTypeStudent = 1;
  static const int userTypeTeacher = 2;
  static const int userTypeAdmin = 3;
  static const int userTypeAlumni = 4;

  // 状态
  static const int statusDisabled = 0;
  static const int statusEnabled = 1;
  static const int statusDeleted = 2;

  // 公告类型
  static const int noticeTypeSchool = 1;
  static const int noticeTypeCollege = 2;
  static const int noticeTypeClass = 3;
  static const int noticeTypePersonal = 4;

  // 报修状态
  static const int repairStatusPending = 0;
  static const int repairStatusProcessing = 1;
  static const int repairStatusCompleted = 2;
  static const int repairStatusClosed = 3;
}
