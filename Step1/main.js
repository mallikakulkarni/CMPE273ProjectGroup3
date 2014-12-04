var writeTemplate = document.querySelector('#write');
writeTemplate.writeNote = function() {
  document.querySelector('#toast2').show();
}

var discardTemplate = document.querySelector('#discard');
discardTemplate.discardNote = function() {
  document.querySelector('#toast1').show();
}



