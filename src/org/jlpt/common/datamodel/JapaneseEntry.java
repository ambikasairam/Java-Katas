package org.jlpt.common.datamodel;

import java.io.Serializable;
import java.util.Objects;
import org.jlpt.common.utils.Validator;

/**
 * Represents a Japanese word written in kana/katakana and romaji, and its English meaning.
 * 
 * @author BJ Peter DeLaCruz
 */
@SuppressWarnings("serial")
public class JapaneseEntry implements Serializable {

  private final String jword;
  private final String reading;
  private final String englishMeaning;

  /**
   * Creates a new JapaneseEntry instance.
   * 
   * @param jword The Japanese word.
   * @param reading The Japanese word reading in kana.
   * @param englishMeaning The English meaning of the Japanese word.
   */
  public JapaneseEntry(String jword, String reading, String englishMeaning) {
    Validator.checkNull(jword);
    Validator.checkNull(reading);
    Validator.checkNull(englishMeaning);

    this.jword = jword;
    this.reading = reading;
    this.englishMeaning = englishMeaning;
  }

  /** @return The Japanese word. */
  public String getJword() {
    return this.jword;
  }

  /** @return The Japanese reading in kana. */
  public String getReading() {
    return this.reading;
  }

  /** @return The English meaning of the Japanese word. */
  public String getEnglishMeaning() {
    return this.englishMeaning;
  }

  /**
   * Returns the entry as a string separated by the given delimiter.
   * @param delimiter The delimiter to separate parts of the entry.
   * @return The entry as a string.
   */
  public String getEntryAsString(String delimiter) {
    return this.jword + delimiter + this.reading + delimiter + this.englishMeaning;
  }

  /** {@inheritDoc} */
  @Override
  public boolean equals(Object o) {
    if (!(o instanceof JapaneseEntry)) {
      return false;
    }
    JapaneseEntry entry = (JapaneseEntry) o;
    return Objects.equals(this.jword, entry.jword) && Objects.equals(this.reading, entry.reading)
        && Objects.equals(this.englishMeaning, entry.englishMeaning);
  }

  /** {@inheritDoc} */
  @Override
  public int hashCode() {
    return Objects.hash(this.jword, this.reading, this.englishMeaning);
  }

  /** {@inheritDoc} */
  @Override
  public String toString() {
    return this.jword + ";" + this.reading + ";" + this.englishMeaning;
  }

}
