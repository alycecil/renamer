package service;

import model.EpisodeTitle;
import model.ShowTitle;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class NewNameRendererTest {
    @Test
    void happyPath() {
        ShowTitle x = new ShowTitle("Naruto (2002)",3);
        EpisodeTitle title = new EpisodeTitle(x, 4);
        assertThat(NewNameRenderer.render(title)).isEqualTo("Naruto (2002) - S03E04");
    }


    @Test
    void happyPath2() {
        ShowTitle x = new ShowTitle("Naruto (2002)",13);
        EpisodeTitle title = new EpisodeTitle(x, 14);
        assertThat(NewNameRenderer.render(title)).isEqualTo("Naruto (2002) - S13E14");
    }


    @Test
    void happyPath3() {
        ShowTitle x = new ShowTitle("Naruto (2002)",123);
        EpisodeTitle title = new EpisodeTitle(x, 146);
        assertThat(NewNameRenderer.render(title)).isEqualTo("Naruto (2002) - S123E146");
    }
}