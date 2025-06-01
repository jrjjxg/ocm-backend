-- 添加课程ID字段到题目表
ALTER TABLE `t_question` ADD COLUMN `course_id` int COMMENT '课程ID' AFTER `subject_id`; 