package com.davidsalas.controller

import com.davidsalas.model.dto.StatDto
import com.davidsalas.service.DnaService
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification

import static org.hamcrest.Matchers.is
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup

class DnaControllerSpec extends Specification {

    DnaService mutantService = Mock()
    MockMvc mockMvc = standaloneSetup(new DnaController(mutantService)).build()

    def "given a mutant DNA chain return HTTP OK"() {
        given:
        mutantService.isMutant(["ATGCGA", "CAGTGC", "TTATGT", "AGAAGG", "CCCCTA", "TCACTG"] as String[]) >> true

        expect:
        postIsMutant(
                """
                        {
                            "dna":["ATGCGA","CAGTGC","TTATGT","AGAAGG","CCCCTA","TCACTG"]
                        }
                        """
        ).andExpect(status().isOk())
    }

    def "given a non mutant DNA chain return HTTP FORBIDDEN"() {
        given:
        mutantService.isMutant(["A", "C", "G", "T", "A", "C"] as String[]) >> false

        expect:
        postIsMutant(
                """
                        {
                            "dna":["A","C","G","T","A","C"]
                        }
                        """
        ).andExpect(status().isForbidden())
    }

    def "given a request to /stats, then return the dna validation stats"() {
        given:
        mutantService.stats() >> new StatDto(8, 2, 0.8D)

        expect:
        getStats()
                .andExpect(status().isOk())
                .andExpect(jsonPath('$.count_mutant_dna', is(8)))
                .andExpect(jsonPath('$.count_human_dna', is(2)))
                .andExpect(jsonPath('$.ratio', is(0.8D)))
    }

    def postIsMutant(String content) {
        mockMvc.perform(
                post("/mutant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
        )
    }

    def getStats() {
        mockMvc.perform(
                get("/stats")
                        .accept(MediaType.APPLICATION_JSON)
        )
    }
}