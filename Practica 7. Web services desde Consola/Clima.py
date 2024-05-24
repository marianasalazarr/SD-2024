import requests

def obtener_datos_meteorologicos(api_key, ciudad):
    url = f"https://api.openweathermap.org/data/2.5/weather?q={ciudad}&appid={api_key}"

    try:
        response = requests.get(url)
        data = response.json()
        if "main" in data and "weather" in data:
            temperatura = data["main"]["temp"] - 273.15  # Convertir de Kelvin a Celsius
            condiciones_climaticas = data["weather"][0]["description"]
            print(f"Temperatura en Londres: {temperatura:.2f}°C")
            print(f"Condiciones Climáticas en Londres: {condiciones_climaticas}")
        else:
            print("Datos meteorológicos no disponibles.")
    except Exception as e:
        print(f"Error: {str(e)}")

if __name__ == "__main__":
    # Tu API key de OpenWeatherMap
    api_key = "768e093b6e408b9be7bb1948456c4798"
    ciudad = "Londres"  # Cambia esto a la ciudad que desees consultar
    obtener_datos_meteorologicos(api_key, ciudad)
