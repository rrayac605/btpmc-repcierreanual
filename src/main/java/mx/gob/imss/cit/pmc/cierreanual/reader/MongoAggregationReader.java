package mx.gob.imss.cit.pmc.cierreanual.reader;

import java.util.*;
import java.util.stream.Collectors;
import lombok.SneakyThrows;
import org.bson.Document;
import org.springframework.batch.item.data.AbstractPaginatedDataItemReader;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

public class MongoAggregationReader<T> extends AbstractPaginatedDataItemReader<T> implements InitializingBean {

    private MongoOperations template;
    private String className;
    private List<AggregationOperation> aggregationOperationList = new ArrayList<>();

    public MongoAggregationReader() {
        super();
        super.setName(ClassUtils.getShortName(MongoAggregationReader.class));
    }

    public void setName(@NonNull String name) {
        super.setName(name);
    }

    public void setTemplate(@NonNull MongoOperations template) {
        this.template = template;
    }

    public void setClassName(@NonNull String className) {
        this.className = className;
    }

    public void setAggregationOperationList(List<AggregationOperation> aggregationOperationList) {
        this.aggregationOperationList = aggregationOperationList;
    }

    @SneakyThrows
    @Override
    @NonNull
    @SuppressWarnings("unchecked")
    protected Iterator<T> doPageRead() {
        ClassLoader classLoader = getClass().getClassLoader();
        Class<?> loadedClass = classLoader.loadClass(className);
        Document cursor = new Document();
        cursor.append("batchSize", pageSize);
        SkipOperation skipOperation = Aggregation.skip((long) page * pageSize);
        aggregationOperationList = aggregationOperationList.stream().filter(Objects::nonNull).collect(Collectors.toList());
        aggregationOperationList.add(skipOperation);
        aggregationOperationList.add(Aggregation.limit(pageSize));
        TypedAggregation<?> aggregation = Aggregation.newAggregation(loadedClass, aggregationOperationList)
        .withOptions(new AggregationOptions.Builder().cursor(cursor).build());
        System.out.println(aggregation.toString());
        return (Iterator<T>) template.aggregate(aggregation, loadedClass).iterator();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.state(template != null, "An implementation of MongoOperations is required.");
        Assert.state(className != null, "A class name to convert the input into is required.");
    }



}
