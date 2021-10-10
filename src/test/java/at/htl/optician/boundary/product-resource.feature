Feature: Store products

  Background:
    * url baseUrl

  Scenario: Store multiple valid should work
    Given path 'products'
    And  request
    """
    [
      {
        "eanCode": "5901234123457",
        "name": "Contact lenses",
        "description": "6*2",
        "price": 20.1,
        "quantity":10
      },
      {
        "eanCode": "4960999667454",
        "name": "Brillenputztuch",
        "description": "In blau und rot erhältlich",
        "price": 10,
        "quantity":50
      },
    ]
    """
    When method POST
    Then status 204

  Scenario: Store multiple with one invalid product should return bad request
    Given path 'products'
    And  request
    """
    [
      {
        "eanCode": "1",
        "name": "Contact lenses",
        "description": "6*2",
        "price": 20.1,
        "quantity":10
      },
      {
        "eanCode": "4960999667454",
        "name": "Brillenputztuch",
        "description": "In blau und rot erhältlich",
        "price": 10,
        "quantity":50
      },
    ]
    """
    When method POST
    Then status 400