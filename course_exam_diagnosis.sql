-- 课程测验功能诊断SQL
-- 用于检查数据库表状态和相关数据

-- 1. 检查相关表是否存在
SELECT 
    TABLE_NAME,
    CASE 
        WHEN TABLE_NAME IS NOT NULL THEN '✅ 存在'
        ELSE '❌ 不存在'
    END AS '表状态'
FROM INFORMATION_SCHEMA.TABLES 
WHERE TABLE_SCHEMA = DATABASE() 
    AND TABLE_NAME IN ('t_course_exam', 't_course_student', 't_exam_paper', 't_exam_paper_answer', 't_course', 't_user')
ORDER BY TABLE_NAME;

-- 2. 检查t_course_exam表结构（如果存在）
SELECT COLUMN_NAME, DATA_TYPE, IS_NULLABLE, COLUMN_DEFAULT
FROM INFORMATION_SCHEMA.COLUMNS 
WHERE TABLE_SCHEMA = DATABASE() 
    AND TABLE_NAME = 't_course_exam'
ORDER BY ORDINAL_POSITION;

-- 3. 检查课程数据
SELECT id, name, description, create_time 
FROM t_course 
ORDER BY id 
LIMIT 10;

-- 4. 检查课程学生关联数据
SELECT cs.*, c.name as course_name, u.real_name as student_name
FROM t_course_student cs
LEFT JOIN t_course c ON cs.course_id = c.id
LEFT JOIN t_user u ON cs.student_id = u.id
WHERE cs.course_id = 1  -- 检查课程ID为1的数据
LIMIT 10;

-- 5. 检查课程测验数据
SELECT ce.*, ep.name as paper_name
FROM t_course_exam ce
LEFT JOIN t_exam_paper ep ON ce.exam_id = ep.id
WHERE ce.course_id = 1  -- 检查课程ID为1的测验
LIMIT 10;

-- 6. 检查试卷数据
SELECT id, name, total_score, question_count, create_time
FROM t_exam_paper
ORDER BY id
LIMIT 5;

-- 7. 检查用户数据（学生）
SELECT id, user_name, real_name, role, status
FROM t_user
WHERE role = 1  -- 学生角色
ORDER BY id
LIMIT 5;

-- 8. 检查是否有示例数据
SELECT 
    '课程' as data_type,
    COUNT(*) as count
FROM t_course
UNION ALL
SELECT 
    '试卷' as data_type,
    COUNT(*) as count
FROM t_exam_paper
UNION ALL
SELECT 
    '课程学生关联' as data_type,
    COUNT(*) as count
FROM t_course_student
UNION ALL
SELECT 
    '课程测验' as data_type,
    COUNT(*) as count
FROM t_course_exam;

-- 9. 诊断结果汇总
SELECT 
    CASE 
        WHEN (SELECT COUNT(*) FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 't_course_exam') = 0 
        THEN '❌ t_course_exam表不存在，这是500错误的主要原因'
        
        WHEN (SELECT COUNT(*) FROM t_course WHERE id = 1) = 0
        THEN '❌ 课程ID=1不存在，请检查课程数据'
        
        WHEN (SELECT COUNT(*) FROM t_course_student WHERE course_id = 1) = 0
        THEN '❌ 没有学生选修课程ID=1，请检查课程学生关联'
        
        WHEN (SELECT COUNT(*) FROM t_course_exam WHERE course_id = 1) = 0
        THEN '⚠️  课程ID=1没有测验数据，这是正常的（会返回空列表）'
        
        ELSE '✅ 数据库状态正常，请检查应用日志获取详细错误信息'
    END AS diagnosis_result; 