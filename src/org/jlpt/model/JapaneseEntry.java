package org.jlpt.model;

import java.io.Serializable;
import org.jlpt.utils.Validator;

/**
 * Represents a Japanese word written in kana/katakana and romaji, and its English meaning.
 * 
 * @author BJ Peter DeLaCruz
 */
@SuppressWarnings("serial")
public class JapaneseEntry implements Serializable {

  private final String jword;
  private final String romaji;
  private final String englishMeaning;

  /**
   * Creates a new JapaneseEntry instance.
   * 
   * @param jword The Japanese word.
   * @param romaji The Japanese word written in romaji.
   * @param englishMeaning The English meaning of the Japanese word.
   */
  public JapaneseEntry(String jword, String romaji, String englishMeaning) {
    Validator.checkNull(jword);
    Validator.checkNull(romaji);
    Validator.checkNull(englishMeaning);

    this.jword = jword;
    this.romaji = romaji;
    this.englishMeaning = englishMeaning;
  }

  /** @return The Japanese word. */
  public String getJword() {
    return this.jword;
  }

  /** @return The Japanese word written in romaji. */
  public String getRomaji() {
    return this.romaji;
  }

  /** @return The English meaning of the Japanese word. */
  public String getEnglishMeaning() {
    return this.englishMeaning;
  }

}
