package com.example.quiz11.service.ifs;

// 這個註解是用來提醒該介面"只能"定義一個方法:所以此介面中有定義第二個方法時，會報錯
// 不需要等到使用 Lambda 表達式來重新定義介面中的方法時，才發現無法使用
@FunctionalInterface
public interface QuizService2 {
	public void test();

//	public void test2();
}
