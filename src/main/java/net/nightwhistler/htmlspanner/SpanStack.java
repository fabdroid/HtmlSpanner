package net.nightwhistler.htmlspanner;

import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.util.Log;
import com.osbcp.cssparser.Rule;
import net.nightwhistler.htmlspanner.css.MatchingRule;
import net.nightwhistler.htmlspanner.style.Style;
import org.htmlcleaner.TagNode;

import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

/**
 * Created with IntelliJ IDEA.
 * User: alex
 * Date: 5/6/13
 * Time: 2:59 PM
 * To change this template use File | Settings | File Templates.
 */
public class SpanStack {

    private Stack<SpanCallback> spanItemStack = new Stack<SpanCallback>();

    private Set<MatchingRule> rules = new HashSet<MatchingRule>();

    public void registerRule( MatchingRule rule ) {

        Log.d("SpanStack", "Registering new Rule: " + rule);

        this.rules.add( rule );
    }

    public Style getStyle( TagNode node, Style baseStyle ) {

        Style result = baseStyle;

        for ( MatchingRule rule: rules ) {
            if ( rule.matches(node) ) {
                Log.d( "SpanStack", "Applying rule " + rule + " to tagNode " + node.getName() );
                result = rule.applyStyle(result);
            }
        }

        return result;
    }

    public void pushSpan( final Object span, final int start, final int end ) {

        SpanCallback callback = new SpanCallback() {
            @Override
            public void applySpan(SpannableStringBuilder builder) {
                builder.setSpan(span, start, end,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        };

        spanItemStack.push(callback);
    }

    public void pushSpan( SpanCallback callback ) {
        spanItemStack.push(callback);
    }

    public void applySpans( SpannableStringBuilder builder ) {
        while ( ! spanItemStack.isEmpty() ) {
            spanItemStack.pop().applySpan(builder);
        }
    }



}