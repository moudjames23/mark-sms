package com.moudjames23.marksms.config;

import com.moudjames23.marksms.batch.CustomerItemProcessor;
import com.moudjames23.marksms.batch.CustomerImportItemWriter;
import com.moudjames23.marksms.model.CustomerImportFieldSetMapper;
import com.moudjames23.marksms.model.entities.Customer;
import com.moudjames23.marksms.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Component;

import java.io.File;

@EnableBatchProcessing
@RequiredArgsConstructor
@Component
@Configuration
public class CustomerImportBatchConfig {

    private static final String JOB_NAME = "customer_job";
    private static final String STEP_NAME = "customer_step";
    private static final String READER_NAME = "customer_reader";

    private final JobBuilderFactory  jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    private final CustomerRepository customerRepository;

    @Value("${header.names}")
    private String columnNames;

    @Value("${delimiter}")
    private String delimiter;


    @Bean
    public Job job(FlatFileItemReader<Customer> itemReader)
    {
        return jobBuilderFactory.get(JOB_NAME)
                .start(step(itemReader))
                .build();
    }

    @Bean
    public Step step(FlatFileItemReader<Customer> itemReader)
    {
        return stepBuilderFactory.get(STEP_NAME)
                .<Customer, Customer>chunk(100)
                .reader(itemReader)
                .processor(itemProcessor())
                .writer(new CustomerImportItemWriter(customerRepository))
                //.taskExecutor(taskExecutor())
                .build();
    }


    @Bean
    @StepScope
    public FlatFileItemReader<Customer> itemReader(@Value("#{jobParameters[fullPathFileName]}") String filePath) {
        FlatFileItemReader<Customer> reader = new FlatFileItemReader<>();
        reader.setName(READER_NAME);
        reader.setResource(new FileSystemResource(new File(filePath)));
        reader.setStrict(false);
        reader.setLineMapper(lineMapper());
        reader.setLinesToSkip(1);

        return reader;
    }

    private LineMapper<Customer> lineMapper() {
        DefaultLineMapper<Customer> lineMapper = new DefaultLineMapper<>();

        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
        tokenizer.setNames(columnNames.split(delimiter));
        tokenizer.setDelimiter(delimiter);

        lineMapper.setLineTokenizer(tokenizer);
        lineMapper.setFieldSetMapper(new CustomerImportFieldSetMapper());

        return lineMapper;

    }

    @Bean
    public ItemProcessor<Customer, Customer> itemProcessor() {
        return new CustomerItemProcessor();
    }





}
