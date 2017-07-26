package org.lucidfox.tengwar;

public enum Glyph {
    TINCO('1'),
    PARMA('q'),
    CALMA('a'),
    QUESSE('z'),
    ANDO('2'),
    UMBAR('w'),
    ANGA('s'),
    UNGWE('x'),
    THULE('3'),
    FORMEN('e'),
    HARMA('d'),
    HWESTA('c'),
    ANTO('4'),
    AMPA('r'),
    ANCA('f'),
    UNQUE('v'),
    NUMEN('5'),
    MALTA('t'),
    NGOLDO('g'),
    NGWALME('b'),
    ORE('6'),
    VALA('y'),
    ANNA('h'),
    VILYA('n'),
    ROMEN('7'),
    ARDA('u'),
    LAMBE('j'),
    ALDA('m'),
    SILME('8'),
    SILME_NUQUERNA('i'),
    ESSE('k'),
    ESSE_NUQUERNA(','),
    HYARMEN('9'),
    HWESTA_SINDARINWA('o'),
    YANTA('l'),
    URE('.'),
    OSSE(']'),
    GASDIL('Ф'),
    TELCO('`'),
    ARA('~'),


    A_LONG('#'),
    A_MEDIUM('E'),
    A_TH('D'),
    A_SHORT('C'),

    E_LONG('$'),
    E_MEDIUM('R'),
    E_TH('F'),
    E_SHORT('V'),

    I_LONG('%'),
    I_MEDIUM('T'),
    I_TH('G'),
    I_SHORT('B'),

    O_LONG('^'),
    O_MEDIUM('Y'),
    O_TH('H'),
    O_SHORT('N'),

    U_LONG('&'),
    U_MEDIUM('U'),
    U_TH('J'),
    U_SHORT('M'),

    WAVE_MEDIUM_HIGH('0'),
    WAVE_MEDIUM_ABOVE('p'),
    WAVE_MEDIUM_BELOW(';'),
    WAVE_MEDIUM_LOW('/'),

    WAVE_LONG_HIGH(')'),
    WAVE_LONG_ABOVE('P'),
    WAVE_LONG_BELOW(':'),
    WAVE_LONG_LOW('?'),

    LINE_MEDIUM_ABOVE('['),
    LINE_MEDIUM_BELOW('\''),

    LINE_LONG_ABOVE('{'),
    LINE_LONG_BELOW('"'),

    W_LONG('è'),
    W_MEDIUM('é'),
    W_TH('ê'),
    W_SHORT('ë'),

    I_LONG_BELOW('('),
    I_MEDIUM_BELOW('O'),
    I_INSIDE('L'),
    A_SHORT_BELOW('>'),

    DIGIT_0('ð'),
    DIGIT_1('ñ'),
    DIGIT_2('ò'),
    DIGIT_3('ó'),
    DIGIT_4('ô'),
    DIGIT_5('õ'),
    DIGIT_6('ö'),
    DIGIT_7('÷'),
    DIGIT_8('ø'),
    DIGIT_9('ù'),
    DIGIT_10('ú'),
    DIGIT_11('û');

    private final char key;

    Glyph(char key) {
        this.key = key;
    }

    public char getKey() {
        return key;
    }

    public static Glyph safeValueOf(String str) {
        try {
            return valueOf(str);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
