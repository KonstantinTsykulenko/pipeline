package com.pipeline.example2;

import com.pipeline.annotation.HandlerMethod;

import java.util.ArrayList;
import java.util.List;

/**
 * @author v-ktsukulenko
 * @since 1/4/13
 */
public class ListOfCatsCreator {
    @HandlerMethod
    public List<Cat> getListOfCats(Integer numberOfCats) {
        List<Cat> cats = new ArrayList<Cat>(numberOfCats);
        for (int i = 0; i < numberOfCats; i++) {
            cats.add(new Cat(i + 1));
        }
        System.out.println(cats);
        return cats;
    }

    private class Cat {
        int catNumber;

        private Cat(int catNumber) {
            this.catNumber = catNumber;
        }

        @Override
        public String toString() {
            return "Cat{" +
                    "catNumber=" + catNumber +
                    '}';
        }
    }
}
