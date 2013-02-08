package com.haxwell.apps.questions.entities;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.haxwell.apps.questions.interfaces.IExam;


/**
 * The persistent class for the EXAM database table.
 * 
 */
@Entity
@Table(name="exam")
public class Exam implements IExam, EntityWithAnIntegerIDBehavior, Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;

	private String title;

	//bi-directional many-to-one association to User
    @ManyToOne
	private User user;

	//uni-directional many-to-many association to Question
    @ManyToMany
	@JoinTable(
		name="exam_question"
		, joinColumns={
			@JoinColumn(name="exam_id")
			}
		, inverseJoinColumns={
			@JoinColumn(name="question_id")
			}
		)
	private Set<Question> questions;

    public Exam() {
    }

    public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	public void addQuestion(Question q) {
		if (this.questions == null)
			this.questions = new HashSet<Question>();
		
		this.questions.add(q);
	}
	
	public Set<Question> getQuestions() {
		if (this.questions == null)
			this.questions = new HashSet<Question>();
		
		return this.questions;
	}

	public void setQuestions(Set<Question> questions) {
		this.questions = questions;
	}
	
	public int getNumberOfQuestions()
	{
		return questions.size();
	}
}