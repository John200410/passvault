package org.passvault.client.utils;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import java.util.regex.Pattern;

/**
 * @author john@chav.is 10/25/2024
 */
public class RegexDocumentFilter extends DocumentFilter {
	
	private final Pattern pattern;
	
	public RegexDocumentFilter(String allowedPattern) {
		this.pattern = Pattern.compile(allowedPattern);
	}
	
	@Override
	public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
		if (!this.pattern.matcher(string).matches()) {
			return;
		}
		super.insertString(fb, offset, string, attr);
	}
	
	@Override
	public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
		if (!this.pattern.matcher(text).matches()) {
			return;
		}
		super.replace(fb, offset, length, text, attrs);
	}
}
