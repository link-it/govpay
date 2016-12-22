

/*
 * Semplice rendering che inverte le righe titolo e sottotitolo 
 */

function versamenti1(item){
	
	var html = '<div two-line="" class="paper-item-body-0">';
	
	html += '<div>';
	html += item.sottotitolo;
	html += '</div>';
	html += '<div secondary="">';
	html += item.titolo;
	html += '</div>';
	html += '</div>';
	
	return html;	
}