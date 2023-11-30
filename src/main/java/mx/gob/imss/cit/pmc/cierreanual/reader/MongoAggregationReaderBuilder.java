package mx.gob.imss.cit.pmc.cierreanual.reader;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.util.Assert;

public class MongoAggregationReaderBuilder<T> {

    private String name;
    private MongoOperations template;
    private String className;
    private List<AggregationOperation> aggregationOperationList = new ArrayList<>();
    private Integer pageSize;

    public MongoAggregationReaderBuilder<T> name(String name) {
        this.name = name;
        return this;
    }

    public MongoAggregationReaderBuilder<T> template(MongoOperations template) {
        this.template = template;
        return this;
    }

    public MongoAggregationReaderBuilder<T> className(String className) {
        this.className = className;
        return this;
    }

    public MongoAggregationReaderBuilder<T> matchOperation(MatchOperation matchOperation) {
        Assert.state(matchOperation != null, "Match operation must not be null");
        aggregationOperationList.add(matchOperation);
        return this;
    }

    public MongoAggregationReaderBuilder<T> lookupOperation(LookupOperation lookupOperation) {
        Assert.state(lookupOperation != null, "Lookup operation must not be null");
        aggregationOperationList.add(lookupOperation);
        return this;
    }

    public MongoAggregationReaderBuilder<T> pageSize(Integer pageSize) {
        this.pageSize = pageSize;
        return this;
    }

    public MongoAggregationReader<T> build() {
        MongoAggregationReader<T> mongoAggregationReader = new MongoAggregationReader<>();
        mongoAggregationReader.setName(name);
        mongoAggregationReader.setClassName(className);
        mongoAggregationReader.setTemplate(template);
        mongoAggregationReader.setAggregationOperationList(aggregationOperationList);
        mongoAggregationReader.setPageSize(pageSize);
        return mongoAggregationReader;
    }

}

