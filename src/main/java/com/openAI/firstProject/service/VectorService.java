package com.openAI.firstProject.service;


import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore; // ✅ correct
import lombok.RequiredArgsConstructor;
import org.springframework.ai.document.Document;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class VectorService {

    private final VectorStore vectorStore;

    private final List<Document> documents = List.of(

            new Document(
                    "My name is Nihal Singh. I am a Java Backend Developer " +
                            "with approximately 1.5 to 2 years of professional experience " +
                            "at TCS. My primary technical stack includes Java, Spring Boot, " +
                            "Spring Cloud, microservices, and REST APIs.",
                    Map.of("category", "personal_profile", "source", "user_context")
            ),

            new Document(
                    "My core technical skills include Java 8, Java 17, Java 21, " +
                            "Spring Boot, Spring Security, Spring Cloud, Hibernate, JPA, " +
                            "RESTful APIs, SQL, and microservices architecture. I am also " +
                            "learning advanced Java concepts, design patterns, and DSA " +
                            "for software engineering interviews.",
                    Map.of("category", "technical_skills", "source", "user_context")
            ),

            new Document(
                    "I have worked with Spring Cloud Gateway, Eureka Service " +
                            "Discovery, Config Server, load balancing, OpenFeign, " +
                            "Redis, circuit breakers, retries, and event-driven " +
                            "microservices concepts. I want to strengthen my understanding " +
                            "of their implementation details and production use cases.",
                    Map.of("category", "microservices", "source", "user_context")
            ),

            new Document(
                    "My professional project experience at TCS includes working " +
                            "with AWS services such as SQS, SNS, EC2, ECS, ECR, S3, IAM " +
                            "roles, and Secrets Manager. I have worked on replacing a " +
                            "hard-coded password with AWS Secrets Manager and improving " +
                            "application startup resilience when retrieving secrets. " +
                            "I have also optimized Hibernate pagination to avoid loading " +
                            "unnecessary database records.",
                    Map.of("category", "professional_experience", "source", "user_context")
            ),

            new Document(
                    "I developed a demo project called EasyBuy using a Spring Boot " +
                            "backend and a separately developed frontend. The application " +
                            "uses Amazon S3 for image storage and Amazon RDS for relational " +
                            "database storage. The backend generates S3 presigned multipart " +
                            "upload URLs, allowing the frontend to upload image parts " +
                            "directly to S3. IAM roles are used for AWS permissions.",
                    Map.of("category", "easybuy_project", "source", "user_context")
            ),

            new Document(
                    "I am learning Spring AI to integrate generative AI capabilities " +
                            "into my existing Spring Boot applications. I have experimented " +
                            "with ChatClient, OpenAI models, prompt engineering, streaming " +
                            "responses, advisors, token usage, and safeguard mechanisms. " +
                            "I am also exploring vector databases, embeddings, and " +
                            "retrieval-augmented generation (RAG).",
                    Map.of("category", "spring_ai", "source", "user_context")
            ),

            new Document(
                    "I have experimented with OpenAI models, including GPT-5-nano, " +
                            "and I am interested in minimizing API costs. I use a MacBook " +
                            "Air with an Apple M4 chip and have explored running local " +
                            "language models through Ollama as an alternative to hosted APIs.",
                    Map.of("category", "ai_tools", "source", "user_context")
            ),

            new Document(
                    "My career goal is to become a Full Stack Java AI Developer. " +
                            "I want to combine Java, Spring Boot, microservices, Spring AI, " +
                            "and frontend development while improving my AWS, Docker, " +
                            "Kubernetes, Jenkins, and CI/CD skills.",
                    Map.of("category", "career_goals", "source", "user_context")
            ),

            new Document(
                    "I am actively preparing for Java backend interviews, focusing " +
                            "on DSA, dynamic programming, Java collections, concurrency, " +
                            "Spring Boot, Spring Security, microservices, AWS, and system " +
                            "design fundamentals. I prefer explanations that include " +
                            "dry runs, edge cases, complexity analysis, and clear reasoning.",
                    Map.of("category", "interview_preparation", "source", "user_context")
            ),

            new Document(
                    "I have practiced Docker, Jenkins pipelines, Kubernetes, " +
                            "Docker Desktop, Amazon EKS, and container image deployment. " +
                            "I prefer beginner-friendly explanations, practical YAML " +
                            "examples, and commands that I can run and verify myself.",
                    Map.of("category", "devops_learning", "source", "user_context")
            ),

            new Document(
                    "When discussing my resume or interview answers, I want " +
                            "accurate and defensible statements. Do not exaggerate my " +
                            "experience or claim that I implemented something unless " +
                            "my actual experience supports it. Distinguish between " +
                            "conceptual knowledge, hands-on implementation, and production " +
                            "experience.",
                    Map.of("category", "response_preferences", "source", "user_context")
            ),

            new Document(
                    "I practice Java DSA problems and prefer solutions that " +
                            "preserve my original approach when possible. I want to improve " +
                            "my problem-solving process by understanding constraints, " +
                            "dry-running examples, identifying edge cases, selecting " +
                            "appropriate data structures, and proving why an algorithm works.",
                    Map.of("category", "dsa_preferences", "source", "user_context")
            )

    );


    public List<Document> getDocuments() {
        return documents;
    }

    public void saveDocument() {
        log.info("Saving document : {}", documents);
        vectorStore.add(this.documents);
    }

    public List<Document> getData(String query){
        List<Document> results = vectorStore.similaritySearch(SearchRequest.builder().query(query).topK(5).similarityThreshold(0.6).build());
        log.info("similarity search results: " + results);
        return results;
    }
}
