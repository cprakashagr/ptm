package ptm;

public class Stemming {

	private char[] b;
	private int i, i_end, j, k;
	private static final int INC = 50;
	
	public Stemming() {
		b = new char[INC];
		i = 0;
		i_end = 0;
	}
	
	public void add(char ch) {
		if (i == b.length) {  
			char[] new_b = new char[i+INC];
			for (int c = 0; c < i; c++) new_b[c] = b[c];
			b = new_b;
		}
		b[i++] = ch;
	}
	
	public void add(char[] w, int wLen) {  
		if (i+wLen >= b.length) {  
			char[] new_b = new char[i+wLen+INC];
			for (int c = 0; c < i; c++) new_b[c] = b[c];
			b = new_b;
		}
		for (int c = 0; c < wLen; c++) 
			b[i++] = w[c];
	}

	public String toString() { 
		return new String(b,0,i_end); 
	}
	
	public int getResultLength() {
		return i_end;
	}
	
	public char[] getResultBuffer() {
		return b;
	}
	
	private final boolean cons(int i) {  
		switch (b[i]) {  
		case 'a': 
		case 'e':
		case 'i':
		case 'o':
		case 'u':
			return false;
	     
		case 'y': 
			return (i==0) ? true : !cons(i-1);
	      
		default: return true;
		}
	}
	
	private final int m() {  
		int n = 0;
		int i = 0;
		while(true)	{  
			if (i > j) return n;
			if (! cons(i)) break; i++;
		}
		i++;
		while(true) {  
			while(true) {  
				if (i > j) 
					return n;
	            
				if (cons(i)) 
					break;
	            
				i++;
			}
			i++;
			n++;
			while(true) {  
				if (i > j) 
					return n;
	         
				
				if (! cons(i))
					break;
				
				i++;
			}
			i++;
		}
	}
	
	private final boolean vowelinstem() {  
		int i; 
		for (i = 0; i <= j; i++) 
			if (! cons(i)) 
				return true;
	   
		return false;
	}
	
	private final boolean doublec(int j) {  
		if (j < 1) 
			return false;
	   
		if (b[j] != b[j-1]) 
			return false;
	   
		return cons(j);
	}
	
	private final boolean cvc(int i) {  
		if (i < 2 || !cons(i) || cons(i-1) || !cons(i-2)) 
			return false;
	   
		int ch = b[i];
		if (ch == 'w' || ch == 'x' || ch == 'y') 
			return false;

		return true;
	}
	
	private final boolean ends(String s) {  
		int l = s.length();
		int o = k-l+1;
		if (o < 0) 
			return false;
		
		for (int i = 0; i < l; i++) 
			if (b[o+i] != s.charAt(i)) 
				return false;
	   
		j = k-l;
		return true;
	}
	
	private final void setto(String s) {  
		int l = s.length();
		int o = j+1;
		for (int i = 0; i < l; i++) 
			b[o+i] = s.charAt(i);
		k = j+l;
	}
	
	private final void r(String s) { 
		if (m() > 0) 
			setto(s); 
	}
	
	private final void step1() {  
		if (b[k] == 's') {  
			if (ends("sses")) 
				k -= 2; 
			else if (ends("ies")) 
					setto("i"); 
			else if (b[k-1] != 's') 
				k--;
		}
		if (ends("eed")) { 
			if (m() > 0) 
				k--; 
			} 
		else if ((ends("ed") || ends("ing")) && vowelinstem()) {  
			k = j;
			if (ends("at")) 
				setto("ate"); 
			else if (ends("bl")) 
				setto("ble"); 
			else if (ends("iz")) 
				setto("ize");
			else if (doublec(k)) {  
				k--;

				int ch = b[k];
				if (ch == 'l' || ch == 's' || ch == 'z') 
					k++;
			}
			else if (m() == 1 && cvc(k)) 
				setto("e");
		}
	}
	
	private final void step2() { 
		if (ends("y") && vowelinstem()) 
			b[k] = 'i'; 
	}
	
	private final void step3() { 
		if (k == 0) 
			return; 
		
		switch (b[k-1]) {
	    case 'a': 
	    	if (ends("ational")) { r("ate"); break; }
	        if (ends("tional")) { r("tion"); break; }
	        break;
	    case 'c': 
	    	if (ends("enci")) { r("ence"); break; }
	    	if (ends("anci")) { r("ance"); break; }
	    	break;
	    case 'e': 
	    	if (ends("izer")) { r("ize"); break; }
	    	break;
	    case 'l': 
	    	if (ends("bli")) { r("ble"); break; }
	    	if (ends("alli")) { r("al"); break; }
	    	if (ends("entli")) { r("ent"); break; }
	    	if (ends("eli")) { r("e"); break; }
	    	if (ends("ousli")) { r("ous"); break; }
	    	break;
	    case 'o': 
	    	if (ends("ization")) { r("ize"); break; }
	    	if (ends("ation")) { r("ate"); break; }
	    	if (ends("ator")) { r("ate"); break; }
	    	break;
	    case 's': 
	    	if (ends("alism")) { r("al"); break; }
	    	if (ends("iveness")) { r("ive"); break; }
	    	if (ends("fulness")) { r("ful"); break; }
	    	if (ends("ousness")) { r("ous"); break; }
	    	break;
	    case 't': 
	    	if (ends("aliti")) { r("al"); break; }
	    	if (ends("iviti")) { r("ive"); break; }
	    	if (ends("biliti")) { r("ble"); break; }
	    	break;
	    case 'g': 
	    	if (ends("logi")) { r("log"); break; }
	
		} 
	}
	
	private final void step4() { 
		switch (b[k]) {
		case 'e': 
			if (ends("icate")) { r("ic"); break; }
			if (ends("ative")) { r(""); break; }
			if (ends("alize")) { r("al"); break; }
			break;
		case 'i': 
			if (ends("iciti")) { r("ic"); break; }
			break;
		case 'l': 
			if (ends("ical")) { r("ic"); break; }
			if (ends("ful")) { r(""); break; }
			break;
		case 's': 
			if (ends("ness")) { r(""); break; }
			break;
		}
	}
	
	private final void step5() {   
		if (k == 0) 
			return;
		switch (b[k-1]) {  
		case 'a': 
			if (ends("al")) break; 
			return;
		case 'c': 
			if (ends("ance")) break;
			if (ends("ence")) break; return;
		case 'e': 
			if (ends("er")) break; 
			return;
		case 'i': 
			if (ends("ic")) break; 
			return;
		case 'l': 
			if (ends("able")) break;
			if (ends("ible")) break; 
			return;
		case 'n': 
			if (ends("ant")) break;
			if (ends("ement")) break;
			if (ends("ment")) break;
			if (ends("ent")) break; 
			return;
		case 'o': 
			if (ends("ion") && j >= 0 && (b[j] == 's' || b[j] == 't')) break;
			if (ends("ou")) break; 
			return;
		case 's': 
			if (ends("ism")) break; 
			return;
		case 't': 
			if (ends("ate")) break;
			if (ends("iti")) break; 
			return;
		case 'u': 
			if (ends("ous")) break; 
			return;
		case 'v': 
			if (ends("ive")) break; 
			return;
		case 'z': 
			if (ends("ize")) break; 
			return;
		default: 
			return;
	    }
	    if (m() > 1) k = j;
	}
	
	private final void step6() {  
		j = k;
		if (b[k] == 'e') {  
			int a = m();
			if (a > 1 || a == 1 && !cvc(k-1)) k--;
		}
		if (b[k] == 'l' && doublec(k) && m() > 1) k--;
	}
	
	public void stem() {  
		k = i - 1;
		if (k > 1) { step1(); step2(); step3(); step4(); step5(); step6(); }
		i_end = k+1; i = 0;
	}
}
