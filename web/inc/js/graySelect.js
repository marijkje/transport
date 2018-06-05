function ShowResults(elem) {
  var selectedOption= elem[elem.selectedIndex];
  if(selectedOption.disabled === true){
            elem.style.color='gray';
        }

        elem.onclick= function(){
            elem.style.color='initial';
        }
    }
 var input = document.getElementsByTagName("select");
 var inputList = Array.prototype.slice.call(input);
 inputList.forEach(ShowResults);
 