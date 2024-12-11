package com.example.quiz11;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.quiz11.service.ifs.QuizService2;

@SpringBootTest
class Quiz11ApplicationTests {

	@Test
	void contextLoads() {
	}

	@Test
	public void lambdaTest() {
		List<Integer> list = new ArrayList<Integer>();
		list.add(1);
		list.add(2);
		list.add(3);

		for (int i = 0; i < list.size(); i++) {
			System.out.println(list.get(i));
		}

		for (Integer item : list) {
			System.out.println(item);
		}

		// lambda
		list.forEach((item) -> {
			System.out.println(item);
		});

		// 小括號變數只有一個時，小括號可省略
		list.forEach(item -> {
			System.out.println(item);
		});

		// 大括號中實作只有一行時，可省略大括號及分號
		list.forEach(item -> System.out.println(item));

		// ============================
		Map<Integer, String> map = new HashMap<>();

		map.put(1, "A");
		map.put(2, "B");
		map.put(3, "C");

		// foreach -- entrySet
		for (Entry<Integer, String> item : map.entrySet()) {
			System.out.println("key: " + item.getKey());
			System.out.println("value: " + item.getValue());
		}

		// foreach -- keySet
		for (Integer item : map.keySet()) {
			System.out.println("key " + item);
			System.out.println("value " + map.get(item));
		}

		// lambda foreach
		map.forEach((k, v) -> {
			System.out.println("key " + k);
			System.out.println("value " + v);
		});

	}

	@Test
	public void ifTest() {
		QuizService2 ifs = new QuizService2() {
			// 重新定義 QuizService 中全部方法
			@Override
			public void test() {
				System.out.println("A");
			}
		};

		System.out.println("B");
		ifs.test();

		// lambda表達式，重新定義所有方法(介面只有定義"一個"方法時使用
		// 因為定義在介面中的test方法沒有參數，所以下方小括號中也不需要有參數
		QuizService2 ifs2 = () -> {
			// 重新定義test方法後的實作內容
			System.out.println("C");
		};
		System.out.println("D");
		ifs2.test();
	}

	@Test
	public void filterTest() {
		List<Integer> list = new ArrayList<>();

		for (int i = 1; i <= 20; i++) {
			list.add(i);
		}

		// 找偶數
		List<Integer> evenList = list.stream().filter((item) -> {
			return item % 2 == 0 && item > 10;
		}).collect(Collectors.toList());

		System.out.println(evenList);
	}

	@Test
	public void functionTest() {
		// Function<T,R>
		// Function<String, Integer> 中的 String 與 Integer
		// String(T):指的是重新定義 apply 方法中的參數資料型態
		// Integer(R): 指的是重新定義 apply 方法執行結果的回傳資料型態
		Function<String, Integer> fun = new Function<>() {

			@Override
			public Integer apply(String t) {
				return 20;
			}
		};

		int test = fun.apply("A");
		System.out.println(test);

	}

	@Test
	public void predicateTest() {
		Predicate<Integer> prd = new Predicate<>() {
			@Override
			public boolean test(Integer t) {
				return t % 2 == 0;
			}
		};

		prd.test(4);
	}
}
