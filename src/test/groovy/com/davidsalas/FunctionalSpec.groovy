package com.davidsalas

import com.davidsalas.model.dto.StatDto
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import spock.lang.Specification

import static org.hamcrest.Matchers.is
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class FunctionalSpec extends Specification {

    MockMvc mockMvc

    @Autowired
    private WebApplicationContext wac

    void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).alwaysDo(MockMvcResultHandlers.print()).build()
    }

    def "given a 10 x 10 matrix with mutant genes return HTTP 200"() {
        expect:
        postIsMutant(
                """
                        {
                          "dna": [
                            "CTGCTACGAT",
                            "TGAGATTAGG",
                            "GCCACGACCC",
                            "ATTGATCAAC",
                            "CTCTTACGCG",
                            "AGGCAAGATA",
                            "TCTGCGTCCA",
                            "TACAGCGTGT",
                            "GTATGATCAC",
                            "CGACACCATT"
                          ]
                        }
                        """
        ).andExpect(status().isOk())
    }

    def "given a 4 x 4 matrix with only one mutant gen return HTTP 403"() {
        expect:
        postIsMutant(
                """
                            {
                                "dna": [
                                    "TTGC",
                                    "AAAA",
                                    "CCGT",
                                    "CTAG"
                               ]
                            }
                        """
        ).andExpect(status().isForbidden())
    }

    def "given a 4 x 4 matrix with two mutant genes in vertical return HTTP 200"() {
        expect:
        postIsMutant(
                """
                            {
                                "dna": [
                                    "ATGT",
                                    "AAAT",
                                    "ACGT",
                                    "ATAT"
                               ]
                            }
                        """
        ).andExpect(status().isOk())
    }

    def "given a 4 x 4 matrix with two mutant genes in horizontal return HTTP 200"() {
        expect:
        postIsMutant(
                """
                            {
                                "dna": [
                                    "TTTT",
                                    "AGTG",
                                    "ATGC",
                                    "TTTT"
                               ]
                            }
                        """
        ).andExpect(status().isOk())
    }

    def "given a 5 x 5 matrix with two mutant genes in diagonal return HTTP 200"() {
        expect:
        postIsMutant(
                """
                            {
                                "dna": [
                                    "TCTTG",
                                    "TTTGG",
                                    "ATGTC",
                                    "TGTTT",
                                    "TGTTG"
                               ]
                            }
                        """
        ).andExpect(status().isOk())
    }

    def "given a 5 x 5 matrix with one mutant gen return HTTP 403"() {
        expect:
        postIsMutant(
                """
                            {
                                "dna": [
                                    "TCTCGCA",
                                    "TGTGGTA",
                                    "ATCGCCA",
                                    "TCCTCCA",
                                    "AGTTGTA",
                                    "TGTTGTA",
                                    "TGTCGTA"
                               ]
                            }
                        """
        ).andExpect(status().isForbidden())
    }

    def "given a 2 x 4 matrix return bad request because the matrix must be NxN"() {
        expect:
        postIsMutant(
                """
                            {
                                "dna": [
                                    "TTGC",
                                    "AAAA"
                               ]
                            }
                        """
        )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath('$.message', is("The imput array must be NxN")))
    }

    def "given a 4 x 4 matrix return bad request because it has an invalid Nucleoid (not G, T, A, C) on right down corner"() {
        expect:
        postIsMutant(
                """
                            {
                                "dna": [
                                    "TTGC",
                                    "AAAA",
                                    "AAAA",
                                    "AAAZ"
                               ]
                            }
                        """
        )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath('$.message', is("DNA's nucleoid must be one of A, T, C, G")))
    }

    def "given a 4 x 4 matrix return bad request because it has an invalid Nucleoid (not G, T, A, C) on left up corner"() {
        expect:
        postIsMutant(
                """
                            {
                                "dna": [
                                    "ZTGC",
                                    "AAAA",
                                    "AAAA",
                                    "AAAA"
                               ]
                            }
                        """
        )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath('$.message', is("DNA's nucleoid must be one of A, T, C, G")))
    }

    def "given two request with mutant dna and one with no mutant dna, then get: human_count 1, humant_dna = 2, ratio = 0.67"() {
        when:
        postIsMutant(
                """
                            {
                                "dna": [
                                    "TTGC",
                                    "AAAA",
                                    "CCGT",
                                    "CTAG"
                               ]
                            }
                        """
        )

        postIsMutant(
                """
                        {
                          "dna": [
                            "CTGCTACGAT",
                            "TGAGATTAGG",
                            "GCCACGACCC",
                            "ATTGATCAAC",
                            "CTCTTACGCG",
                            "AGGCCAGATA",
                            "TCTGCGTCCA",
                            "TACAGCGTGT",
                            "GTATGATCAC",
                            "CGACACCATT"
                          ]
                        }
                        """
        )

        postIsMutant(
                """
                        {
                          "dna": [
                            "CTGCTACGAT",
                            "TGAGATTAGG",
                            "GCCACGACCC",
                            "ATTGATCAAC",
                            "CTCTTACGCG",
                            "AGGCAAGATA",
                            "TCTGCGTCCA",
                            "TACAGCGTGT",
                            "GTATGATCAC",
                            "CGACACCATT"
                          ]
                        }
                        """
        )

        def response = getStats()

        then:
        response == new StatDto(2, 1, 0.67D)
    }

    def "given a matrix with mutant genes on horizontal at edges return HTTP 200"() {
        expect:
        postIsMutant(
                """
                        {
                            "dna": [
                                "TTTT",
                                "AACC",
                                "ACAC",
                                "AAAA"
                            ]
                        }
                        """
        )
                .andExpect(status().isOk())
    }

    def "given a matrix with mutant genes on horizontal at center return HTTP 200"() {
        expect:
        postIsMutant(
                """
                        {
                            "dna": [
                                "TTTC",
                                "AAAA",
                                "CCCC",
                                "AAAG"
                            ]
                        }
                        """
        )
                .andExpect(status().isOk())
    }

    def "given a matrix with mutant genes on vertical at edges return HTTP 200"() {
        expect:
        postIsMutant(
                """
                        {
                            "dna": [
                                "ATTC",
                                "AAAC",
                                "ACCC",
                                "AAAC"
                            ]
                        }
                        """
        )
                .andExpect(status().isOk())
    }

    def "given a matrix with mutant genes on vertical at center return HTTP 200"() {
        expect:
        postIsMutant(
                """
                        {
                            "dna": [
                                "CTGC",
                                "ATGG",
                                "ATGC",
                                "ATGC"
                            ]
                        }
                        """
        )
                .andExpect(status().isOk())
    }

    def "given a matrix with only one on vertical at center return HTTP 403"() {
        expect:
        postIsMutant(
                """
                        {
                            "dna": [
                                "CTGC",
                                "ATCG",
                                "ATGC",
                                "ATGC"
                            ]
                        }
                        """
        )
                .andExpect(status().isForbidden())
    }

    def "given a matrix with only one on hortizontal at center return HTTP 403"() {
        expect:
        postIsMutant(
                """
                       {
                            "dna": [
                                "CTGC",
                                "CCCC",
                                "ATGC",
                                "ATGG"
                            ]
                        }
                        """
        )
                .andExpect(status().isForbidden())
    }

    def "given a matrix with only two mutants on diagonal at the center inclined to the right return HTTP 200"() {
        expect:
        postIsMutant(
                """
                       {
                            "dna": [
                                "CTGA",
                                "CCAC",
                                "AACC",
                                "ATGC"
                            ]
                        }
                        """
        )
                .andExpect(status().isOk())
    }

    def "given a matrix with two mutants on diagonal at the edge inclined to the right return HTTP 200"() {
        expect:
        postIsMutant(
                """
                       {
                            "dna": [
                                "CTGAA",
                                "CCAAA",
                                "AGAAA",
                                "AAACA",
                                "AAGCA"
                            ]
                        }
                        """
        )
                .andExpect(status().isOk())
    }

    def "given a matrix with only one mutant on diagonal at the edge inclined to the right return HTTP 403"() {
        expect:
        postIsMutant(
                """
                       {
                            "dna": [
                                "CTGAA",
                                "CCAAA",
                                "AGGAG",
                                "AAACA",
                                "AAGCA"
                            ]
                        }
                        """
        )
                .andExpect(status().isForbidden())
    }


    def postIsMutant(String content) {
        mockMvc.perform(
                post("/mutant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
        )
    }

    def getStats() {
        ObjectMapper mapper = new ObjectMapper()
        mapper.readValue(
                mockMvc.perform(
                        get("/stats")
                                .accept(MediaType.APPLICATION_JSON)
                ).andReturn().getResponse().contentAsString,
                StatDto.class
        )

    }
}