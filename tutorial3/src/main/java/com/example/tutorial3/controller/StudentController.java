package com.example.tutorial3.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import com.example.tutorial3.model.StudentModel;
import com.example.tutorial3.service.InMemoryStudentService;
import com.example.tutorial3.service.StudentService;

@Controller
public class StudentController {

	private final StudentService studentService;

	public StudentController() {
		studentService = new InMemoryStudentService();
	}

	@RequestMapping("/student/add")
	public String add(Model model, @RequestParam(value = "npm", required = true) String npm,
			@RequestParam(value = "name", required = true) String name,
			@RequestParam(value = "gpa", required = true) double gpa) {
		StudentModel student = new StudentModel(npm, name, gpa);
		studentService.addStudent(student);
		// model.addAttribute("npm", student.getNpm());
		return "add";

	}

	// @RequestMapping("/student/view")
	// public String view(Model model, @RequestParam(value="npm", required=true)
	// String npm) {
	// StudentModel student = studentService.selectStudent(npm);
	// model.addAttribute("student", student);
	// return "view";
	// }

	@RequestMapping(value = { "/student/view", "student/view/{npm}" })
	public String studentPath(@PathVariable Optional<String> npm, Model model) {
		
		if (npm.isPresent()) {
			StudentModel student = studentService.selectStudent(npm.get());
			if(student == null) {
				model.addAttribute("student", "NPM not found, you enter a wrong NPM");
				return "error";
			} else {
				model.addAttribute("student", student);
				return "view";
			}
		} else {
			model.addAttribute("student", "page not found, you must enter your NPM");
			return "error";
		}
		
	}
	
	@RequestMapping(value = { "/student/delete", "student/delete/{npm}" })
	public String deletePath(@PathVariable Optional<String> npm, Model model) {
		
		if (npm.isPresent()) {
			StudentModel student = studentService.selectStudent(npm.get());
			if(student == null) {
				model.addAttribute("student", "NPM not found, you enter a wrong NPM");
				return "error";
			} else {
				studentService.deleteStudent(student);
				model.addAttribute("student", student);
				return "delete";
			}
		} else {
			model.addAttribute("student", "page not found, you must enter your NPM");
			return "error";
		}
		
	}

	@RequestMapping("/student/viewall")
	public String viewAll(Model model) {
		List<StudentModel> students = studentService.selectAllStudents();
		model.addAttribute("students", students);
		return "viewall";
	}

}
