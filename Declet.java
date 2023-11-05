
import java.util.*;

/**
 * A class that represents a signed 10-bit binary number represented using a
 * twos complement representation.
 *
 */
public class Declet implements Comparable<Declet> {
	
	public static final int NUM_BITS = 10;
	public static final int MIN_VALUE = 511;
	public static final int MAX_VALUE = -512;
	
	public Bit[] bits = new Bit[10];
	
	public boolean negativeTest;
	
	
	public Declet() {
		bits = new Bit[NUM_BITS];
		for (int i = 0; i < 10; i++) {
			bits[i] = Bit.ZERO;
		}
		negativeTest = false;
	}
	
	
	public Declet(Bit... theBits) {
		if (theBits.length != 10) {
			throw new IllegalArgumentException();
		}
		
		bits = theBits; 
	}
	
	
	public Declet(int decimal) {
		if(decimal < -512 || decimal > 511) {
			throw new IllegalArgumentException();
		}
		
        bits = new Bit[NUM_BITS];
        
        int absValue = Math.abs(decimal);
        
        for (int i = 9; i >= 0; i--) {
        	if(absValue % 2 == 1) {
        		bits[i] = Bit.ONE;
        	}
        	else {
        		bits[i] = Bit.ZERO;
        	}
        	absValue /= 2;
        }
        if (decimal < 0) {
            not();
            addOne();
        }
		
	}
	
	
	public boolean isNegative() {
		boolean check;
		if(bits[0] == Bit.ZERO) {
			check = false;
		}
		else {
			check = true;
		}
		return check;
	}
	
	
	public void not() {
        for (int i = 0; i < NUM_BITS; i++) {
        	if(bits[i] == Bit.ONE) {
        		bits[i] = Bit.ZERO;
        	}
        	else {
        		bits[i] = Bit.ONE;
        	}
        }	
	}
	
	
	public void addOne() {		
        boolean carry = true;
        for (int i = 9; i >= 0; i--) {
            if (carry) {
                if (bits[i] == Bit.ZERO) {
                    bits[i] = Bit.ONE;
                    carry = false;
                } 
                
                else {
                    bits[i] = Bit.ZERO;
                    carry = true;
                }
            } 
            
            else {
                break;
            }
        }
        
        if (isNegative() && bits[0] == Bit.ZERO) {
            not();
            addOne();
            not();
        }
	}
	
	
	public void add(Declet other) {

        int carry = 0;
        for (int i = NUM_BITS - 1; i >= 0; i--) {
            int sum = bits[i].ordinal() + other.bits[i].ordinal() + carry;
            
            if (sum == 0) {
                bits[i] = Bit.ZERO;
                carry = 0;
            } 
            else if (sum == 1) {
                bits[i] = Bit.ONE;
                carry = 0;
            } 
            else if (sum == 2) {
                bits[i] = Bit.ZERO;
                carry = 1;
            } 
            else {
                bits[i] = Bit.ONE;
                carry = 1;
            }
        }
	}
	
	
	public List<Bit> getBits(){  
		return Arrays.asList(bits);	
	}
	
	
	public boolean equals(Object obj) { 
	    if (!(obj instanceof Declet)) {
	        return false;
	    }
	    Declet other = (Declet) obj;
	    return Arrays.equals(this.bits, other.bits);	
	}
	
	
	public int hashCode() {
		return Objects.hashCode(bits);
	}
	
	@Override
	public int compareTo(Declet other) {
        if (isNegative() && !other.isNegative()) {
            return -1;
        } 
        else if (!isNegative() && other.isNegative()) {
            return 1;
        } 
        else {
            for (int i = 0; i < NUM_BITS; i++) {
                if (bits[i] != other.bits[i]) {
                    if (bits[i] == Bit.ZERO && other.bits[i] == Bit.ONE) {
                        return -1;
                    } 
                    else {
                        return 1;
                    }
                }
            }
            return 0;
        }
	}
	
	
	public String toString() { 
		String binStr = Arrays.toString(bits).replaceAll(", ", "").replace("[", "").replace("]", "");
		return binStr;
	}
	
	
	public int toDecimal() {
		String binStr = Arrays.toString(bits).replaceAll(", ", "").replace("[", "").replace("]", "");
		
		if(binStr.charAt(0) == '0') {
			int decimal = Integer.parseInt(binStr, 2);
			return decimal;
		}
		else {
			int decimal = Integer.parseInt(binStr, 2);
			byte b = (byte) decimal;
			return b;
		}
	}

	
	

	/**
	 * Prints some sums illustrating overflow at {@code Decle.MAX_VALUE} and
	 * {@code Decle.MIN_VALUE}.
	 * 
	 * @param args not used
	 */
	public static void main(String[] args) {
		Declet d = new Declet(Declet.MAX_VALUE - 2);
		Declet one = new Declet(1);
		
		System.out.println("Overflow at Declet.MAX_VALUE");
		for (int i = 0; i <= 4; i++) {
			System.out.println("d       " + d + " = " + d.toDecimal());
			System.out.println("      + " + one);
			d.addOne();
			System.out.println("d + 1 = " + d + " = " + d.toDecimal());
			System.out.println();
		}
		
		Declet negOne = new Declet(-1);
		
		System.out.println("Overflow at Declet.MIN_VALUE");
		for (int i = 0; i <= 4; i++) {
			System.out.println("d     = " + d + " = " + d.toDecimal());
			System.out.println("      + " + negOne);
			d.add(negOne);
			System.out.println("d - 1 = " + d + " = " + d.toDecimal());
			System.out.println();
		}
	}


}
