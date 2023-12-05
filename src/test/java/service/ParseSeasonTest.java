package service;


import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ParseSeasonTest {

    @Test
    void seasonCheckBasic() {
        assertThat(ParseSeason.parse("Season 1")).isEqualTo(1);
    }


    @Test
    void seasonCheck0() {
        assertThat(ParseSeason.parse("1029 Season 00001")).isEqualTo(1);
    }

    @Test
    void seasonCheck1() {
        assertThat(ParseSeason.parse("Fr3e Text 1029 Season 100001")).isEqualTo(100001);
    }


    @Test
    void seasonCheck2() {
        assertThat(ParseSeason.parse("2")).isEqualTo(2);
    }

    @Test
    void seasonCheck3() {
        assertThat(ParseSeason.parse("1029 Season 9999 00001 ")).isEqualTo(1);
    }

    @Test
    void seasonCheck4() {
        assertThat(ParseSeason.parse("  Fr3e Text 1029 Season 100001 ")).isEqualTo(100001);
    }


    @Test
    void seasonCheck5() {
        assertThat(ParseSeason.parse(" 2  ")).isEqualTo(2);
    }
}