insert into parametres_file(id_parametres_file,folder_animes,folder_films,folder_racine,folder_series) values
(0,'/animes','/films','C:/Users/latou/Downloads/conv mp4/Hubstream','/series');

update parametresfile set folder_animes='' where idParametresFile=1;

update parametresfile set folder_films='' where idParametresFile=1;

update parametresfile set folder_series='' where idParametresFile=1;

update parametresfile set folder_racine='' where idParametresFile=1;