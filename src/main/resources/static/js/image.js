<script>
            var myCanvas;
             var myContext;
             let imgInput = document.getElementById('inputGroupFile04');
             imgInput.addEventListener('change', function (e) {
                 if (e.target.files) {
                     let imageFile = e.target.files[0]; //here we get the image file
                     var reader = new FileReader();
                     reader.readAsDataURL(imageFile); 
                     reader.onloadend = function (e) {
                         console.log(">>>>>>>>>Entering>>>>>>>");
                         var myImage = new Image(); // Creates image object
                         myImage.src = e.target.result; // Assigns converted image to image object
                         myImage.onload = function (ev) {
                             myCanvas = document.getElementById("myCanvas"); // Creates a canvas object
                             myContext = myCanvas.getContext("2d"); // Creates a contect object
                             myCanvas.width = myImage.width; // Assigns image's width to canvas
                             myCanvas.height = myImage.height; // Assigns image's height to canvas
                             myContext.drawImage(myImage, 0, 0); // Draws the image on canvas
                             let imgData = myCanvas.toDataURL("image/jpeg", 0.75); // Assigns image base64 string in jpeg format to a variable
                             initCanvas(myContext);
                         }
                     }
                 }
             });
         // Selecting 3 points 
         var count = 0;
         function initCanvas(e) {
             // document.body.style.backgroundColor = "pink";
             var c = document.getElementById('myCanvas').getContext('2d');
             // var img = document.getElementById("imageInput");
             var rect = myCanvas.getBoundingClientRect();
             e.canvas.addEventListener('click', function (event) {
                 if (count < 3) { 
                     // var posx = (e.clientX-rect.left)/(rect.right - rect.left)*myCanvas.width;
                     // var posy = (e.clientY-rect.top)/(rect.bottom-rect-rect.top)*myCanvas.height;
                     let x=event.clientX - rect.left - myCanvas.clientLeft;
                     let y=event.clientY - rect.top - myCanvas.clientTop;
                         e.fillStyle = "#000000";
                         e.fillRect (x, y, 6, 6);
                    
                     console.log("Coordinate x: " + x +"Coordinate y: " + y);
                     
                     count++;
                     console.log(">>>>>>>>>Entering>>>>>>>");
                     
                 }
             });
             
         }
         </script>