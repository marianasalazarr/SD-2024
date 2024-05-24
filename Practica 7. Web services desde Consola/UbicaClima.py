import requests

def obtener_informacion_ubicacion(geonames_username, lugar):
    url = f"http://api.geonames.org/searchJSON?name={lugar}&maxRows=1&username={geonames_username}"
    try:
        response = requests.get(url)
        data = response.json()
        if "geonames" in data and data["geonames"]:
            ubicacion = data["geonames"][0]
            nombre = ubicacion['name']
            pais = ubicacion['countryName']
            return nombre, pais
        else:
            print("Ubicación no encontrada.")
            return None, None
    except Exception as e:
        print(f"Error: {str(e)}")
        return None, None

def obtener_datos_meteorologicos(api_key, ciudad):
    url = f"https://api.openweathermap.org/data/2.5/weather?q={ciudad}&appid={api_key}"
    try:
        response = requests.get(url)
        data = response.json()
        if "main" in data and "weather" in data:
            temperatura = data["main"]["temp"] - 273.15  # Convertir de Kelvin a Celsius
            condiciones_climaticas = data["weather"][0]["description"]
            return temperatura, condiciones_climaticas
        else:
            print("Datos meteorológicos no disponibles.")
            return None, None
    except Exception as e:
        print(f"Error: {str(e)}")
        return None, None

if __name__ == "__main__":
    # Coloca tu usuario de geonames
    geonames_username = "marchii"
    # Tu API key de OpenWeatherMap
    api_key = "768e093b6e408b9be7bb1948456c4798"

    lugar = "Argentina"  # Cambia esto a la ubicación que desees consultar

    # Obtener información de la ubicación
    ciudad, pais = obtener_informacion_ubicacion(geonames_username, lugar)
    if ciudad and pais:
        # Obtener datos meteorológicos
        temperatura, condiciones_climaticas = obtener_datos_meteorologicos(api_key, ciudad)
        if temperatura is not None and condiciones_climaticas is not None:
            print(f"Temperatura en {ciudad}, {pais}: {temperatura:.2f}°C")
            print(f"Condiciones Climáticas en {ciudad}, {pais}: {condiciones_climaticas}")

