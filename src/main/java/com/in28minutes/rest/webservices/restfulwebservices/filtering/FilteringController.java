package com.in28minutes.rest.webservices.restfulwebservices.filtering;

import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.PropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Arrays;
import java.util.List;

@RestController
public class FilteringController {

    @GetMapping("/filtering")
    public MappingJacksonValue retrieveSomeBean() {
        SomeBean someBean = new SomeBean("value1", "value2", "value3");

        MappingJacksonValue mapping = filterSomeBean(someBean, "field1", "field2");

        return mapping;
    }

    @GetMapping("/filtering-list")
    public MappingJacksonValue retrieveListOfSomeBeans() {
        List<SomeBean> someBeanList = Arrays.asList(
            new SomeBean("value11", "value12", "value13"),
            new SomeBean("value21", "value22", "value23"),
            new SomeBean("value31", "value23", "value33")
        );

        MappingJacksonValue mapping = filterSomeBean(someBeanList, "field2", "field3");

        return mapping;
    }

    private static MappingJacksonValue filterSomeBean(Object object, String... fields) {
        PropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept(fields);
        FilterProvider filterProvider = new SimpleFilterProvider().addFilter(SomeBean.FILTER_NAME, filter);
        MappingJacksonValue mapping = new MappingJacksonValue(object);
        mapping.setFilters(filterProvider);
        return mapping;
    }
}
