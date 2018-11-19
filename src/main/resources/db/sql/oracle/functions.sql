-- Funzione necessaria per calcolare il numero di millisecondi dal 1/1/1970 perche' non e' nativa in oracle.
create OR REPLACE FUNCTION date_to_unix_for_smart_order (p_date  date,in_src_tz in varchar2 default 'Europe/Kiev') return number is
begin
    return round((cast((FROM_TZ(CAST(p_date as timestamp), in_src_tz) at time zone 'GMT') as date)-TO_DATE('01.01.1970','dd.mm.yyyy'))*(24*60*60));
end;
