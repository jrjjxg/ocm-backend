-- MySQL dump 10.13  Distrib 9.0.0, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: exam
-- ------------------------------------------------------
-- Server version	9.0.0

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `t_assignment`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_assignment` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '作业唯一标识',
  `title` varchar(255) COLLATE utf8mb4_general_ci NOT NULL COMMENT '作业标题',
  `description` text COLLATE utf8mb4_general_ci NOT NULL COMMENT '作业描述与要求',
  `attachment_url` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '作业附件文件URL',
  `start_time` datetime NOT NULL COMMENT '作业开始时间',
  `end_time` datetime NOT NULL COMMENT '作业截止时间',
  `course_id` bigint unsigned NOT NULL COMMENT '关联课程表ID (t_course.id)',
  `creator_id` int NOT NULL COMMENT '作业创建者（教师）ID (t_user.id)',
  `total_score` decimal(5,2) NOT NULL COMMENT '作业满分值',
  `status` int NOT NULL COMMENT '作业状态(1:未开始,2:进行中,3:已结束)',
  `create_time` datetime NOT NULL COMMENT '作业创建时间',
  `update_time` datetime NOT NULL COMMENT '作业信息更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_assignment_course_id` (`course_id`),
  KEY `idx_assignment_creator_id` (`creator_id`),
  CONSTRAINT `fk_assignment_course` FOREIGN KEY (`course_id`) REFERENCES `t_course` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_assignment_creator` FOREIGN KEY (`creator_id`) REFERENCES `t_user` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='存储教师布置的作业信息';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_assignment`
--


--
-- Table structure for table `t_assignment_submit`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_assignment_submit` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '提交记录唯一标识',
  `assignment_id` bigint unsigned NOT NULL COMMENT '关联作业表ID (t_assignment.id)',
  `student_id` int NOT NULL COMMENT '关联学生用户ID (t_user.id)',
  `content` text COLLATE utf8mb4_general_ci COMMENT '学生提交作业的文字说明',
  `attachment_url` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '学生提交的作业文件URL',
  `submit_time` datetime NOT NULL COMMENT '学生提交作业的时间',
  `status` int NOT NULL COMMENT '批改状态(1:未批改,2:已批改)',
  `score` decimal(5,2) DEFAULT NULL COMMENT '作业得分',
  `comment` text COLLATE utf8mb4_general_ci COMMENT '教师批改评语',
  `check_time` datetime DEFAULT NULL COMMENT '教师批改时间',
  `teacher_id` int DEFAULT NULL COMMENT '批改教师ID (t_user.id)',
  PRIMARY KEY (`id`),
  KEY `idx_submit_assignment_id` (`assignment_id`),
  KEY `idx_submit_student_id` (`student_id`),
  KEY `idx_submit_teacher_id` (`teacher_id`),
  CONSTRAINT `fk_submit_assignment` FOREIGN KEY (`assignment_id`) REFERENCES `t_assignment` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_submit_student` FOREIGN KEY (`student_id`) REFERENCES `t_user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_submit_teacher` FOREIGN KEY (`teacher_id`) REFERENCES `t_user` (`id`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='存储学生提交的作业及评分情况';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_assignment_submit`
--


--
-- Table structure for table `t_course`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_course` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '课程唯一标识',
  `name` varchar(255) COLLATE utf8mb4_general_ci NOT NULL COMMENT '课程名称',
  `code` varchar(255) COLLATE utf8mb4_general_ci NOT NULL COMMENT '课程代码',
  `description` text COLLATE utf8mb4_general_ci COMMENT '课程详细描述',
  `semester` varchar(255) COLLATE utf8mb4_general_ci NOT NULL COMMENT '开课学期',
  `credit` decimal(5,2) NOT NULL COMMENT '课程学分',
  `status` int NOT NULL COMMENT '课程状态(1:启用,0:禁用)',
  `creator_id` int NOT NULL COMMENT '课程创建者（教师）ID (t_user.id)',
  `create_time` datetime NOT NULL COMMENT '课程创建时间',
  `update_time` datetime NOT NULL COMMENT '课程信息更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_course_code` (`code`),
  KEY `idx_course_creator_id` (`creator_id`),
  CONSTRAINT `fk_course_creator` FOREIGN KEY (`creator_id`) REFERENCES `t_user` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='存储课程的基本信息';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_course`
--


--
-- Table structure for table `t_course_student`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_course_student` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '关联记录唯一标识',
  `course_id` bigint unsigned NOT NULL COMMENT '关联课程表ID (t_course.id)',
  `student_id` int NOT NULL COMMENT '关联学生用户ID (t_user.id)',
  `status` int NOT NULL COMMENT '选课状态(1:已选,2:已退)',
  `final_score` decimal(5,2) DEFAULT NULL COMMENT '学生课程最终成绩',
  `creator_id` int DEFAULT NULL COMMENT '关联记录创建者ID (t_user.id)',
  `create_time` datetime NOT NULL COMMENT '关联记录创建时间',
  `update_time` datetime NOT NULL COMMENT '关联记录更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_course_student` (`course_id`,`student_id`),
  KEY `idx_course_student_course_id` (`course_id`),
  KEY `idx_course_student_student_id` (`student_id`),
  KEY `idx_course_student_creator_id` (`creator_id`),
  CONSTRAINT `fk_course_student_course` FOREIGN KEY (`course_id`) REFERENCES `t_course` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_course_student_creator` FOREIGN KEY (`creator_id`) REFERENCES `t_user` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_course_student_student` FOREIGN KEY (`student_id`) REFERENCES `t_user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='存储课程与学生的关联关系';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_course_student`
--


--
-- Table structure for table `t_course_teacher`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_course_teacher` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '关联记录唯一标识',
  `course_id` bigint unsigned NOT NULL COMMENT '关联课程表ID (t_course.id)',
  `teacher_id` int NOT NULL COMMENT '关联教师用户ID (t_user.id)',
  `auth_type` int NOT NULL COMMENT '教师对课程的权限类型',
  `creator_id` int NOT NULL COMMENT '关联记录创建者（管理员）ID (t_user.id)',
  `create_time` datetime NOT NULL COMMENT '关联记录创建时间',
  `update_time` datetime NOT NULL COMMENT '关联记录更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_course_teacher` (`course_id`,`teacher_id`),
  KEY `idx_course_teacher_course_id` (`course_id`),
  KEY `idx_course_teacher_teacher_id` (`teacher_id`),
  KEY `idx_course_teacher_creator_id` (`creator_id`),
  CONSTRAINT `fk_course_teacher_course` FOREIGN KEY (`course_id`) REFERENCES `t_course` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_course_teacher_creator` FOREIGN KEY (`creator_id`) REFERENCES `t_user` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `fk_course_teacher_teacher` FOREIGN KEY (`teacher_id`) REFERENCES `t_user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='存储课程与教师的关联关系';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_course_teacher`
--


--
-- Table structure for table `t_discussion`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_discussion` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '讨论主题唯一标识',
  `title` varchar(255) COLLATE utf8mb4_general_ci NOT NULL COMMENT '讨论主题标题',
  `content` text COLLATE utf8mb4_general_ci NOT NULL COMMENT '讨论主题内容',
  `course_id` bigint unsigned NOT NULL COMMENT '关联课程表ID (t_course.id)',
  `creator_id` int NOT NULL COMMENT '主题创建者ID (t_user.id)',
  `creator_type` int NOT NULL COMMENT '创建者类型(1:学生,2:教师)',
  `view_count` int NOT NULL DEFAULT '0' COMMENT '主题被浏览次数',
  `reply_count` int NOT NULL DEFAULT '0' COMMENT '主题被回复次数',
  `is_top` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否置顶(true:置顶)',
  `is_essence` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否精华(true:精华)',
  `create_time` datetime NOT NULL COMMENT '主题创建时间',
  `update_time` datetime NOT NULL COMMENT '主题信息更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_discussion_course_id` (`course_id`),
  KEY `idx_discussion_creator_id` (`creator_id`),
  CONSTRAINT `fk_discussion_course` FOREIGN KEY (`course_id`) REFERENCES `t_course` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_discussion_creator` FOREIGN KEY (`creator_id`) REFERENCES `t_user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='存储课程讨论区的主题帖';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_discussion`
--


--
-- Table structure for table `t_discussion_reply`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_discussion_reply` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '回复唯一标识',
  `discussion_id` bigint unsigned NOT NULL COMMENT '关联讨论主题表ID (t_discussion.id)',
  `content` text COLLATE utf8mb4_general_ci NOT NULL COMMENT '回复内容',
  `replier_id` int NOT NULL COMMENT '回复者用户ID (t_user.id)',
  `replier_type` int NOT NULL COMMENT '回复者类型(1:学生,2:教师)',
  `parent_id` bigint unsigned DEFAULT NULL COMMENT '回复的父评论ID，用于二级回复 (t_discussion_reply.id)',
  `create_time` datetime NOT NULL COMMENT '回复创建时间',
  `update_time` datetime NOT NULL COMMENT '回复信息更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_reply_discussion_id` (`discussion_id`),
  KEY `idx_reply_replier_id` (`replier_id`),
  KEY `idx_reply_parent_id` (`parent_id`),
  CONSTRAINT `fk_reply_discussion` FOREIGN KEY (`discussion_id`) REFERENCES `t_discussion` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_reply_parent` FOREIGN KEY (`parent_id`) REFERENCES `t_discussion_reply` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_reply_replier` FOREIGN KEY (`replier_id`) REFERENCES `t_user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='存储讨论主题的回复内容';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_discussion_reply`
--


--
-- Table structure for table `t_paper_question`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_paper_question` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '关联记录唯一标识',
  `paper_id` int NOT NULL COMMENT '关联试卷表ID (t_exam_paper.id)',
  `question_id` int NOT NULL COMMENT '关联题库表ID (t_question.id)',
  `question_number` int NOT NULL COMMENT '试卷中的题目序号',
  `score` decimal(5,2) NOT NULL COMMENT '该题在试卷中的分值',
  `create_time` datetime NOT NULL COMMENT '关联记录创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_paper_question` (`paper_id`,`question_id`,`question_number`),
  KEY `idx_pq_paper_id` (`paper_id`),
  KEY `idx_pq_question_id` (`question_id`),
  CONSTRAINT `fk_pq_paper` FOREIGN KEY (`paper_id`) REFERENCES `t_exam_paper` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_pq_question` FOREIGN KEY (`question_id`) REFERENCES `t_question` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='存储试卷与题目的关联关系';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_paper_question`
--


--
-- Table structure for table `t_resource`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_resource` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '资源唯一标识',
  `title` varchar(255) COLLATE utf8mb4_general_ci NOT NULL COMMENT '资源标题',
  `description` text COLLATE utf8mb4_general_ci COMMENT '资源描述',
  `type` int NOT NULL COMMENT '资源类型(1:文档,2:PPT,3:视频,4:音频)',
  `url` varchar(255) COLLATE utf8mb4_general_ci NOT NULL COMMENT '资源文件存储路径',
  `file_size` bigint NOT NULL COMMENT '文件大小(字节)',
  `file_type` varchar(255) COLLATE utf8mb4_general_ci NOT NULL COMMENT '文件MIME类型',
  `course_id` bigint unsigned NOT NULL COMMENT '关联课程表ID (t_course.id)',
  `uploader_id` int NOT NULL COMMENT '资源上传者（教师）ID (t_user.id)',
  `create_time` datetime NOT NULL COMMENT '资源创建时间',
  `update_time` datetime NOT NULL COMMENT '资源信息更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_resource_course_id` (`course_id`),
  KEY `idx_resource_uploader_id` (`uploader_id`),
  CONSTRAINT `fk_resource_course` FOREIGN KEY (`course_id`) REFERENCES `t_course` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_resource_uploader` FOREIGN KEY (`uploader_id`) REFERENCES `t_user` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='存储课程相关的教学资源';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_resource`
--


--
-- Table structure for table `t_role`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_role` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '角色唯一标识',
  `name` varchar(255) COLLATE utf8mb4_general_ci NOT NULL COMMENT '角色名称',
  `code` varchar(255) COLLATE utf8mb4_general_ci NOT NULL COMMENT '角色编码',
  `description` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '角色功能描述',
  `create_time` datetime NOT NULL COMMENT '角色创建时间',
  `update_time` datetime NOT NULL COMMENT '角色信息更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_role_name` (`name`),
  UNIQUE KEY `uk_role_code` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='存储系统中不同角色的定义';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_role`
--


--
-- Table structure for table `t_user_role`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_user_role` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '关联记录唯一标识',
  `user_id` int NOT NULL COMMENT '关联用户表ID (t_user.id)',
  `role_id` bigint unsigned NOT NULL COMMENT '关联角色表ID (t_role.id)',
  `create_time` datetime NOT NULL COMMENT '关联记录创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_role_user_id` (`user_id`),
  KEY `idx_user_role_role_id` (`role_id`),
  CONSTRAINT `fk_user_role_role` FOREIGN KEY (`role_id`) REFERENCES `t_role` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_user_role_user` FOREIGN KEY (`user_id`) REFERENCES `t_user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='存储用户和角色的多对多关系';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_user_role`
--


--
-- Table structure for table `t_exam_paper`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_exam_paper` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `subject_id` int DEFAULT NULL,
  `paper_type` int DEFAULT NULL,
  `grade_level` int DEFAULT NULL,
  `score` int DEFAULT NULL,
  `question_count` int DEFAULT NULL,
  `suggest_time` int DEFAULT NULL,
  `limit_start_time` datetime DEFAULT NULL,
  `limit_end_time` datetime DEFAULT NULL,
  `frame_text_content_id` int DEFAULT NULL,
  `create_user` int DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `deleted` bit(1) DEFAULT NULL,
  `task_exam_id` int DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=COMPACT;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_exam_paper`
--


--
-- Table structure for table `t_exam_paper_answer`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_exam_paper_answer` (
  `id` int NOT NULL AUTO_INCREMENT,
  `exam_paper_id` int DEFAULT NULL,
  `paper_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `paper_type` int DEFAULT NULL,
  `subject_id` int DEFAULT NULL,
  `system_score` int DEFAULT NULL,
  `user_score` int DEFAULT NULL,
  `paper_score` int DEFAULT NULL,
  `question_correct` int DEFAULT NULL,
  `question_count` int DEFAULT NULL,
  `do_time` int DEFAULT NULL,
  `status` int DEFAULT NULL,
  `create_user` int DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `task_exam_id` int DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=COMPACT;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_exam_paper_answer`
--


--
-- Table structure for table `t_exam_paper_question_customer_answer`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_exam_paper_question_customer_answer` (
  `id` int NOT NULL AUTO_INCREMENT,
  `question_id` int DEFAULT NULL,
  `exam_paper_id` int DEFAULT NULL,
  `exam_paper_answer_id` int DEFAULT NULL,
  `question_type` int DEFAULT NULL,
  `subject_id` int DEFAULT NULL,
  `customer_score` int DEFAULT NULL,
  `question_score` int DEFAULT NULL,
  `question_text_content_id` int DEFAULT NULL,
  `answer` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `text_content_id` int DEFAULT NULL,
  `do_right` bit(1) DEFAULT NULL,
  `create_user` int DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `item_order` int DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=COMPACT;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_exam_paper_question_customer_answer`
--


--
-- Table structure for table `t_message`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_message` (
  `id` int NOT NULL AUTO_INCREMENT,
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `content` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `send_user_id` int DEFAULT NULL,
  `send_user_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `send_real_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `receive_user_count` int DEFAULT NULL,
  `read_count` int DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=COMPACT;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_message`
--


--
-- Table structure for table `t_message_user`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_message_user` (
  `id` int NOT NULL AUTO_INCREMENT,
  `message_id` int DEFAULT NULL,
  `receive_user_id` int DEFAULT NULL,
  `receive_user_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `receive_real_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `readed` bit(1) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `read_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=COMPACT;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_message_user`
--


--
-- Table structure for table `t_question`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_question` (
  `id` int NOT NULL AUTO_INCREMENT,
  `question_type` int DEFAULT NULL,
  `subject_id` int DEFAULT NULL,
  `score` int DEFAULT NULL,
  `grade_level` int DEFAULT NULL,
  `difficult` int DEFAULT NULL,
  `correct` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
  `info_text_content_id` int DEFAULT NULL,
  `create_user` int DEFAULT NULL,
  `status` int DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `deleted` bit(1) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=COMPACT;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_question`
--


--
-- Table structure for table `t_subject`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_subject` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `level` int DEFAULT NULL,
  `level_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `item_order` int DEFAULT NULL,
  `deleted` bit(1) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=COMPACT;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_subject`
--


--
-- Table structure for table `t_task_exam`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_task_exam` (
  `id` int NOT NULL AUTO_INCREMENT,
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `grade_level` int DEFAULT NULL,
  `frame_text_content_id` int DEFAULT NULL,
  `create_user` int DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `deleted` bit(1) DEFAULT NULL,
  `create_user_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=COMPACT;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_task_exam`
--


--
-- Table structure for table `t_task_exam_customer_answer`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_task_exam_customer_answer` (
  `id` int NOT NULL AUTO_INCREMENT,
  `task_exam_id` int DEFAULT NULL,
  `create_user` int DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `text_content_id` int DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=COMPACT;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_task_exam_customer_answer`
--


--
-- Table structure for table `t_text_content`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_text_content` (
  `id` int NOT NULL AUTO_INCREMENT,
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=COMPACT;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_text_content`
--


--
-- Table structure for table `t_user`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_user` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_uuid` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `user_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `real_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `age` int DEFAULT NULL,
  `sex` int DEFAULT NULL,
  `birth_day` datetime DEFAULT NULL,
  `user_level` int DEFAULT NULL,
  `phone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `role` int DEFAULT NULL,
  `status` int DEFAULT NULL,
  `image_path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `modify_time` datetime DEFAULT NULL,
  `last_active_time` datetime DEFAULT NULL,
  `deleted` bit(1) DEFAULT NULL,
  `wx_open_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=COMPACT;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_user`
--

INSERT INTO `t_user` VALUES (1,'d2d29da2-dcb3-4013-b874-727626236f47','student','D1AGFL+Gx37t0NPG4d6biYP5Z31cNbwhK5w1lUeiHB2zagqbk8efYfSjYoh1Z/j1dkiRjHU+b0EpwzCh8IGsksJjzD65ci5LsnodQVf4Uj6D3pwoscXGqmkjjpzvSJbx42swwNTA+QoDU8YLo7JhtbUK2X0qCjFGpd+8eJ5BGvk=','学生',18,1,'2019-09-01 16:00:00',1,'19171171610',1,1,'https://www.mindskip.net:9008/image/ba607a75-83ba-4530-8e23-660b72dc4953/头像.jpg','2019-09-07 18:55:02','2020-02-04 08:26:54',NULL,_binary '\0',NULL),(2,'52045f5f-a13f-4ccc-93dd-f7ee8270ad4c','admin','D1AGFL+Gx37t0NPG4d6biYP5Z31cNbwhK5w1lUeiHB2zagqbk8efYfSjYoh1Z/j1dkiRjHU+b0EpwzCh8IGsksJjzD65ci5LsnodQVf4Uj6D3pwoscXGqmkjjpzvSJbx42swwNTA+QoDU8YLo7JhtbUK2X0qCjFGpd+8eJ5BGvk=','管理员',30,1,'2019-09-07 18:56:07',NULL,NULL,3,1,NULL,'2019-09-07 18:56:21',NULL,NULL,_binary '\0',NULL),(3,'d8a91842-8ed7-48f5-9421-203da4b22d3b','jrj','CYN7BmF0VVTK6LYd61w6FxMw2ZqrdZLt6gtgioRsXSvBrHBpzH+lJS4HCr8WpAfzXOqfRAhl/+7WNjX2BPbO3mHYlG1ayc8KRVY9hDGKUwjiSwZ/eOdrzxAckQNVFSyD0yzcQR7WeqMTnutStgRxtphqu2kn4Nw/r0k28R4c4ag=',NULL,NULL,NULL,NULL,1,NULL,1,1,NULL,'2025-05-20 15:24:18',NULL,'2025-05-20 15:24:18',_binary '\0',NULL);

--
-- Table structure for table `t_user_event_log`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_user_event_log` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int DEFAULT NULL,
  `user_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `real_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=COMPACT;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_user_event_log`
--

INSERT INTO `t_user_event_log` VALUES (1,3,'jrj',NULL,'欢迎 jrj 注册来到学之思开源考试系统','2025-05-20 15:24:18'),(2,3,'jrj',NULL,'jrj 登录了学之思开源考试系统','2025-05-20 15:24:24'),(3,2,'admin','管理员','admin 登录了学之思开源考试系统','2025-05-30 08:34:38'),(4,2,'admin','管理员','admin 登出了学之思开源考试系统','2025-05-30 08:34:45'),(5,2,'admin','管理员','admin 登录了学之思开源考试系统','2025-05-30 08:46:56'),(6,1,'student','学生','student 登录了学之思开源考试系统','2025-05-30 08:52:42');

--
-- Table structure for table `t_user_token`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_user_token` (
  `id` int NOT NULL AUTO_INCREMENT,
  `token` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `user_id` int DEFAULT NULL,
  `wx_open_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `end_time` datetime DEFAULT NULL,
  `user_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=COMPACT;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_user_token`
--


--
-- Dumping events for database 'exam'
--

--
-- Dumping routines for database 'exam'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-05-30  9:21:00
