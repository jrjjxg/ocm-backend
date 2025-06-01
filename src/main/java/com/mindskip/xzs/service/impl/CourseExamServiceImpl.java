package com.mindskip.xzs.service.impl;

import com.mindskip.xzs.domain.CourseExam;
import com.mindskip.xzs.domain.User;
import com.mindskip.xzs.repository.CourseExamMapper;
import com.mindskip.xzs.service.CourseExamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 课程测验服务实现类
 */
@Service
public class CourseExamServiceImpl extends BaseServiceImpl<CourseExam> implements CourseExamService {
    
    private final CourseExamMapper courseExamMapper;
    
    @Autowired
    public CourseExamServiceImpl(CourseExamMapper courseExamMapper) {
        super(courseExamMapper);
        this.courseExamMapper = courseExamMapper;
    }
    
    @Override
    public List<CourseExam> findByCourseId(Long courseId) {
        return courseExamMapper.findByCourseId(courseId);
    }
    
    @Override
    public CourseExam findByCourseIdAndExamId(Long courseId, Integer examId) {
        return courseExamMapper.findByCourseIdAndExamId(courseId, examId);
    }
    
    @Override
    public CourseExam create(CourseExam courseExam, User user) {
        Date now = new Date();
        courseExam.setCreatorId(user.getId());
        courseExam.setCreateTime(now);
        courseExam.setUpdateTime(now);
        
        // 设置默认值
        if (courseExam.getShuffleQuestions() == null) {
            courseExam.setShuffleQuestions(false);
        }
        if (courseExam.getShowResult() == null) {
            courseExam.setShowResult(true);
        }
        if (courseExam.getLimitIp() == null) {
            courseExam.setLimitIp(false);
        }
        
        courseExamMapper.insertSelective(courseExam);
        return courseExam;
    }
    
    @Override
    public CourseExam update(CourseExam courseExam) {
        courseExam.setUpdateTime(new Date());
        courseExamMapper.updateByPrimaryKeySelective(courseExam);
        return courseExam;
    }
    
    @Override
    public void delete(Long courseId, Integer examId) {
        courseExamMapper.deleteByCourseIdAndExamId(courseId, examId);
    }
    
    @Override
    public CourseExam selectById(Long id) {
        return courseExamMapper.selectByPrimaryKeyLong(id);
    }
    
    @Override
    public int deleteById(Long id) {
        return courseExamMapper.deleteByPrimaryKeyLong(id);
    }
} 