-- 课程测验功能修复SQL
-- 创建课程测验表
CREATE TABLE IF NOT EXISTS `t_course_exam` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `course_id` bigint(20) NOT NULL COMMENT '课程ID',
  `exam_id` int(11) NOT NULL COMMENT '试卷ID（关联t_exam_paper表）',
  `title` varchar(255) NOT NULL COMMENT '测验标题',
  `description` longtext COMMENT '测验描述',
  `start_time` datetime NOT NULL COMMENT '开始时间',
  `end_time` datetime NOT NULL COMMENT '结束时间',
  `duration` int(11) DEFAULT 0 COMMENT '时长（分钟），0表示不限时',
  `shuffle_questions` bit(1) DEFAULT b'0' COMMENT '是否随机打乱题目',
  `show_result` bit(1) DEFAULT b'1' COMMENT '是否显示结果',
  `limit_ip` bit(1) DEFAULT b'0' COMMENT '是否限制IP',
  `status` int(1) DEFAULT 1 COMMENT '状态：1-未开始，2-进行中，3-已结束，4-已取消',
  `creator_id` int(11) NOT NULL COMMENT '创建者ID',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_course_id` (`course_id`),
  KEY `idx_exam_id` (`exam_id`),
  KEY `idx_start_time` (`start_time`),
  KEY `idx_end_time` (`end_time`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='课程测验表';

-- 检查课程学生关联表是否存在
CREATE TABLE IF NOT EXISTS `t_course_student` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `course_id` bigint(20) NOT NULL COMMENT '课程ID',
  `student_id` int(11) NOT NULL COMMENT '学生ID',
  `enrollment_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '选课时间',
  `status` int(1) DEFAULT 1 COMMENT '状态：1-正常，2-退课',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_course_student` (`course_id`, `student_id`),
  KEY `idx_course_id` (`course_id`),
  KEY `idx_student_id` (`student_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='课程学生关联表';

-- 插入一些测试数据（如果需要的话）
-- 假设课程ID为1，学生ID为当前登录用户
INSERT IGNORE INTO `t_course_student` (`course_id`, `student_id`, `status`) VALUES 
(1, 1, 1),
(1, 2, 1);

-- 插入一些测试测验数据（如果已有试卷的话）
-- 这里假设已经有试卷ID为1的试卷
INSERT IGNORE INTO `t_course_exam` (`course_id`, `exam_id`, `title`, `description`, `start_time`, `end_time`, `duration`, `creator_id`) VALUES 
(1, 1, '第一次课程测验', '这是第一次课程测验，请认真完成。', '2024-01-01 09:00:00', '2024-12-31 18:00:00', 60, 1); 