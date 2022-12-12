package com.extract.text.textextract;

import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigInteger;

public interface TextExtractRepository extends JpaRepository<NewsRelease, BigInteger> {
}
