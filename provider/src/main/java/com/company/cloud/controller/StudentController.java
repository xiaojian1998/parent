package com.company.cloud.controller;

import com.company.cloud.model.Student;
import com.company.cloud.model.Studentquery;
import com.mongodb.client.result.DeleteResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
public class StudentController {
    @Autowired
    MongoTemplate mongoTemplate;

    @RequestMapping("list")
    public Map list(Studentquery studentquery){
        Map<String,Object> map = new HashMap<>();
        Query query = new Query();

        if(studentquery.getName()!=null){
            query.addCriteria(Criteria.where("name").regex(".*"+studentquery.getName()+".*"));
        }
        if(studentquery.getAge()!=null){
            query.addCriteria(Criteria.where("age").regex(".*"+studentquery.getAge()+".*"));
        }

        List<Student> all = mongoTemplate.find(query,Student.class);
        long count = mongoTemplate.count(query, Student.class);
        map.put("all",all);

        return map;
    }

    /**
     * 删除
     * @param id
     * @return
     */
    @RequestMapping("del")
    public boolean del(String id){
        Student student = mongoTemplate.findById(id, Student.class);
        DeleteResult remove = mongoTemplate.remove(student);
        return true;
    }


    /**
     * 回显
     * @param ids
     * @return
     */
    @RequestMapping("getbyid")
    public Object getbyid(String ids){

        Student student = mongoTemplate.findById(ids, Student.class);

        return student;
    }


    /**
     * 添加修改
     * @param student
     * @return
     */
    @RequestMapping("save")
    public boolean save(@RequestBody Student student){

        if(student.getId()!=null){
            Query query = Query.query(Criteria.where("id").is(student.getId()));
            Update update =  new Update();
            update.set("name",student.getName());
            update.set("age",student.getAge());


            mongoTemplate.updateFirst(query,update,Student.class);
            return true;
        }
        mongoTemplate.insert(student);
        return true;
    }


}
