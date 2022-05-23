package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

//all'interno di questa classe andremo ad eseguire i nostri test
class DemoApplicationTests {

	//esempio di test

	Calculator underTest = new Calculator();

	@Test //JUnit Test
	void itShouldAddNumbers() {
		//given
		int numberOne = 20;
		int numberTwo = 30;

		//when
		int result = underTest.add(numberOne, numberTwo);

		//then - dove faccio l'asserzione, l'affermazione, se è corretta il test passa
		int expected = 50;
		assertThat(result).isEqualTo(expected);
	}

	class Calculator{
		int add(int a, int b) {
			return a + b;
		}
	}

}
